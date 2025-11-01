package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.sistema.domain.Accion;
import pe.edu.unasam.activos.modules.sistema.dto.AccionDTO;
import pe.edu.unasam.activos.modules.sistema.repository.AccionRepository;

import java.util.List;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccionService {

    private final AccionRepository accionRepository;

    @Transactional(readOnly = true)
    public Page<AccionDTO.Response> getAllAcciones(Pageable pageable) {
        return accionRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<AccionDTO.Response> getAllAcciones(String query, Pageable pageable) {
        if (query != null && !query.isBlank()) {
            return accionRepository.findByNombreAccionContainingIgnoreCase(query.trim(), pageable)
                    .map(this::convertToDto);
        }
        return accionRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<AccionDTO.Response> getAllAccionesAsList() {
        return accionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AccionDTO.Response> getAccionById(Integer id) {
        return accionRepository.findById(id).map(this::convertToDto);
    }

    public AccionDTO.Response createAccion(AccionDTO.Request request) {
        if (request.getNombreAccion() != null && accionRepository.existsByNombreAccion(request.getNombreAccion())) {
            throw new BusinessException("El nombre de acción '" + request.getNombreAccion() + "' ya existe.");
        }

        String codigoGenerado = generateCodigo(request.getNombreAccion());
        if (codigoGenerado != null && accionRepository.existsByCodigoAccion(codigoGenerado)) {
            throw new BusinessException("El código de acción '" + codigoGenerado + "' ya existe.");
        }

        Accion accion = new Accion();
        accion.setNombreAccion(request.getNombreAccion());
        accion.setCodigoAccion(codigoGenerado);
        accion.setDescripcionAccion(request.getDescripcionAccion());
        Accion savedAccion = accionRepository.save(accion);
        return convertToDto(savedAccion);
    }

    public AccionDTO.Response updateAccion(Integer id, AccionDTO.Request request) {
        Accion accion = accionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Acción no encontrada con ID: " + id));

        if (request.getNombreAccion() != null) {
            accionRepository.findByNombreAccion(request.getNombreAccion()).ifPresent(existing -> {
                if (!existing.getIdAccion().equals(id)) {
                    throw new BusinessException("El nombre de acción '" + request.getNombreAccion() + "' ya existe.");
                }
            });
        }

        accion.setNombreAccion(request.getNombreAccion());
        String codigoGenerado = generateCodigo(request.getNombreAccion() != null ? request.getNombreAccion() : accion.getNombreAccion());
        // Validación de duplicado del código generado
        accionRepository.findByCodigoAccion(codigoGenerado).ifPresent(existing -> {
            if (!existing.getIdAccion().equals(id)) {
                throw new BusinessException("El código de acción '" + codigoGenerado + "' ya existe.");
            }
        });
        accion.setCodigoAccion(codigoGenerado);
        accion.setDescripcionAccion(request.getDescripcionAccion());
        Accion updatedAccion = accionRepository.save(accion);
        return convertToDto(updatedAccion);
    }

    public void deleteAccion(Integer id) {
        if (!accionRepository.existsById(id)) {
            throw new NotFoundException("Acción no encontrada con ID: " + id);
        }
        accionRepository.deleteById(id);
    }

    private AccionDTO.Response convertToDto(Accion accion) {
        return AccionDTO.Response.builder()
                .idAccion(accion.getIdAccion())
                .nombreAccion(accion.getNombreAccion())
                .codigoAccion(accion.getCodigoAccion())
                .descripcionAccion(accion.getDescripcionAccion())
                .build();
    }

    // Genera código seguro desde el nombre (mayúsculas, sin tildes, conectores removidos, guiones bajos)
    private String generateCodigo(String nombre) {
        if (nombre == null) return null;
        String s = Normalizer.normalize(nombre, Normalizer.Form.NFD);
        // elimina diacríticos
        s = s.replaceAll("\\p{M}+", "");
        s = s.toUpperCase(Locale.ROOT).trim();
        // conectaores comunes: DE, Y, A
        s = s.replaceAll("\\b(DE|Y|A)\\b", " ");
        // Solo letras y números
        s = s.replaceAll("[^A-Z0-9]+", " ");
        // espacios a underscore
        s = s.trim().replaceAll("\\s+", "_");
        // Ajustar longitud máxima al esquema (20)
        if (s.length() > 20) s = s.substring(0, 20);
        return s;
    }
}
