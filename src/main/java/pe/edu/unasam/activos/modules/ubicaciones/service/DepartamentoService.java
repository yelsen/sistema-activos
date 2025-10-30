package pe.edu.unasam.activos.modules.ubicaciones.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Departamento;
import pe.edu.unasam.activos.modules.ubicaciones.dto.DepartamentoDTO;
import pe.edu.unasam.activos.modules.ubicaciones.repository.DepartamentoRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    @Transactional(readOnly = true)
    public Page<DepartamentoDTO.Response> getAllDepartamentos(String query, Pageable pageable) {
        Page<Departamento> page;
        if (query != null && !query.isBlank()) {
            page = departamentoRepository.findByNombreDepartamentoContainingIgnoreCase(query.trim(), pageable);
        } else {
            page = departamentoRepository.findAll(pageable);
        }
        return page.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public DepartamentoDTO.Response getDepartamentoById(Integer id) {
        return departamentoRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado con ID: " + id));
    }

    public DepartamentoDTO.Response createDepartamento(DepartamentoDTO.Request request) {
        String nombre = (request.getNombreDepartamento() != null) ? request.getNombreDepartamento().trim() : "";
        if (nombre.isBlank()) {
            throw new BusinessException("El nombre del departamento es obligatorio.");
        }
        if (departamentoRepository.existsByNombreDepartamento(nombre)) {
            throw new BusinessException("Ya existe un departamento con el nombre '" + nombre + "'.");
        }
        Departamento departamento = Departamento.builder()
                .nombreDepartamento(nombre)
                .build();
        return convertToDto(departamentoRepository.save(departamento));
    }

    public DepartamentoDTO.Response updateDepartamento(Integer id, DepartamentoDTO.Request request) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado con ID: " + id));

        String nombre = (request.getNombreDepartamento() != null) ? request.getNombreDepartamento().trim() : "";
        if (nombre.isBlank()) {
            throw new BusinessException("El nombre del departamento es obligatorio.");
        }

        Optional<Departamento> existente = departamentoRepository.findByNombreDepartamento(nombre);
        if (existente.isPresent() && !existente.get().getIdDepartamento().equals(id)) {
            throw new BusinessException("Ya existe un departamento con el nombre '" + nombre + "'.");
        }

        departamento.setNombreDepartamento(nombre);
        return convertToDto(departamentoRepository.save(departamento));
    }

    public void deleteDepartamento(Integer id) {
        departamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado con ID: " + id));
        departamentoRepository.deleteById(id);
    }

    private DepartamentoDTO.Response convertToDto(Departamento d) {
        return new DepartamentoDTO.Response(d.getIdDepartamento(), d.getNombreDepartamento());
    }
}
