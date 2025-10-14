package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.sistema.domain.Accion;
import pe.edu.unasam.activos.modules.sistema.dto.AccionDTO;
import pe.edu.unasam.activos.modules.sistema.repository.AccionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccionService {

    private final AccionRepository accionRepository;

    @Transactional(readOnly = true)
    public List<AccionDTO.Response> getAllAcciones() {
        return accionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AccionDTO.Response> getAccionById(Integer id) {
        return accionRepository.findById(id).map(this::convertToDto);
    }

    public AccionDTO.Response createAccion(AccionDTO.Request request) {
        Accion accion = new Accion();
        accion.setNombreAccion(request.getNombreAccion());
        accion.setCodigoAccion(request.getCodigoAccion());
        accion.setDescripcionAccion(request.getDescripcionAccion());
        Accion savedAccion = accionRepository.save(accion);
        return convertToDto(savedAccion);
    }

    public AccionDTO.Response updateAccion(Integer id, AccionDTO.Request request) {
        Accion accion = accionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Accion not found with id: " + id));
        accion.setNombreAccion(request.getNombreAccion());
        accion.setCodigoAccion(request.getCodigoAccion());
        accion.setDescripcionAccion(request.getDescripcionAccion());
        Accion updatedAccion = accionRepository.save(accion);
        return convertToDto(updatedAccion);
    }

    public void deleteAccion(Integer id) {
        if (!accionRepository.existsById(id)) {
            throw new NotFoundException("Accion not found with id: " + id);
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
}
