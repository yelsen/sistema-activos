package pe.edu.unasam.activos.modules.ubicaciones.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Origen;
import pe.edu.unasam.activos.modules.ubicaciones.dto.OrigenDTO;
import pe.edu.unasam.activos.modules.ubicaciones.repository.OrigenRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OrigenService {

    private final OrigenRepository origenRepository;

    @Transactional(readOnly = true)
    public Page<OrigenDTO.Response> listar(String query, Pageable pageable) {
        String q = trimToNull(query);
        if (q != null) {
            return origenRepository.findByNombreOrigenContainingIgnoreCase(q, pageable)
                    .map(this::toDto);
        }
        return origenRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public OrigenDTO.Response getById(Integer id) {
        Origen origen = origenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Origen no encontrado con ID: " + id));
        return toDto(origen);
    }

    public OrigenDTO.Response crear(OrigenDTO.Request request) {
        String nombre = trimToNull(request.getNombreOrigen());
        if (nombre == null) {
            throw new BusinessException("El nombre del origen es obligatorio.");
        }
        if (origenRepository.existsByNombreOrigenIgnoreCase(nombre)) {
            throw new BusinessException("Ya existe un origen con el nombre '" + nombre + "'.");
        }
        Origen origen = Origen.builder()
                .nombreOrigen(nombre)
                .build();
        return toDto(origenRepository.save(origen));
    }

    public OrigenDTO.Response actualizar(Integer id, OrigenDTO.Request request) {
        Origen origen = origenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Origen no encontrado con ID: " + id));
        String nombre = trimToNull(request.getNombreOrigen());
        if (nombre == null) {
            throw new BusinessException("El nombre del origen es obligatorio.");
        }
        origenRepository.findByNombreOrigen(nombre).ifPresent(existing -> {
            if (!existing.getIdOrigen().equals(id)) {
                throw new BusinessException("Ya existe un origen con el nombre '" + nombre + "'.");
            }
        });
        origen.setNombreOrigen(nombre);
        return toDto(origenRepository.save(origen));
    }

    public void eliminar(Integer id) {
        Origen origen = origenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Origen no encontrado con ID: " + id));
        origenRepository.delete(origen);
    }

    private OrigenDTO.Response toDto(Origen origen) {
        return OrigenDTO.Response.builder()
                .idOrigen(origen.getIdOrigen())
                .nombreOrigen(origen.getNombreOrigen())
                .build();
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
