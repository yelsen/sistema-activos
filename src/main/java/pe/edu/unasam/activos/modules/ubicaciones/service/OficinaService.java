package pe.edu.unasam.activos.modules.ubicaciones.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.enums.EstadoOficina;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.ubicaciones.dto.OficinaDTO;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import pe.edu.unasam.activos.modules.ubicaciones.repository.OficinaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OficinaService {

    private final OficinaRepository oficinaRepository;

    @Transactional(readOnly = true)
    public List<OficinaDTO.Response> getAllOficinasAsList() {
        return oficinaRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<OficinaDTO.Response> listarPaginado(String query, EstadoOficina estado, Pageable pageable) {
        String q = trimToNull(query);
        boolean hasQuery = (q != null);
        boolean hasEstado = (estado != null);

        Page<Oficina> page;
        if (hasQuery && hasEstado) {
            page = oficinaRepository
                    .findByNombreOficinaContainingIgnoreCaseAndEstadoOficina(q, estado, pageable);
        } else if (hasQuery) {
            page = oficinaRepository.findByNombreOficinaContainingIgnoreCase(q, pageable);
        } else if (hasEstado) {
            page = oficinaRepository.findByEstadoOficina(estado, pageable);
        } else {
            page = oficinaRepository.findAll(pageable);
        }
        return page.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public OficinaDTO.Response getById(Integer id) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada con ID: " + id));
        return toDto(oficina);
    }

    public OficinaDTO.Response create(OficinaDTO.Request request) {
        String nombre = trimToNull(request.getNombreOficina());
        if (nombre == null) {
            throw new BusinessException("El nombre de la oficina es obligatorio.");
        }
        if (oficinaRepository.existsByNombreOficinaIgnoreCase(nombre)) {
            throw new BusinessException("Ya existe una oficina con el nombre '" + nombre + "'.");
        }
        Oficina oficina = Oficina.builder()
                .nombreOficina(nombre)
                .direccionOficina(request.getDireccionOficina())
                .telefonoOficina(request.getTelefonoOficina())
                .estadoOficina(EstadoOficina.ACTIVO)
                .build();
        return toDto(oficinaRepository.save(oficina));
    }

    public OficinaDTO.Response update(Integer id, OficinaDTO.Request request) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada con ID: " + id));

        String nombre = trimToNull(request.getNombreOficina());
        if (nombre == null) {
            throw new BusinessException("El nombre de la oficina es obligatorio.");
        }
        if (!oficina.getNombreOficina().equalsIgnoreCase(nombre)
                && oficinaRepository.existsByNombreOficinaIgnoreCase(nombre)) {
            throw new BusinessException("Ya existe una oficina con el nombre '" + nombre + "'.");
        }

        oficina.setNombreOficina(nombre);
        oficina.setDireccionOficina(request.getDireccionOficina());
        oficina.setTelefonoOficina(request.getTelefonoOficina());
        // Regla del usuario: al editar siempre activo
        oficina.setEstadoOficina(EstadoOficina.ACTIVO);

        return toDto(oficinaRepository.save(oficina));
    }

    public void delete(Integer id) {
        try {
            oficinaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("Oficina no encontrada con ID: " + id);
        }
    }

    public OficinaDTO.Response marcarEnRemodelacion(Integer id) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada con ID: " + id));
        if (oficina.getEstadoOficina() != EstadoOficina.ACTIVO) {
            throw new BusinessException("Solo puede marcarse en remodelación una oficina ACTIVA.");
        }
        oficina.setEstadoOficina(EstadoOficina.EN_REMODELACION);
        return toDto(oficinaRepository.save(oficina));
    }

    public OficinaDTO.Response toggleEstado(Integer id) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Oficina no encontrada con ID: " + id));
        if (oficina.getEstadoOficina() == EstadoOficina.INACTIVO) {
            throw new BusinessException("No se puede cambiar el estado de una oficina inactiva.");
        }
        EstadoOficina nuevoEstado = (oficina.getEstadoOficina() == EstadoOficina.ACTIVO)
                ? EstadoOficina.EN_REMODELACION
                : EstadoOficina.ACTIVO;
        oficina.setEstadoOficina(nuevoEstado);
        return toDto(oficinaRepository.save(oficina));
    }

    private OficinaDTO.Response toDto(Oficina oficina) {
        return OficinaDTO.Response.builder()
                .idOficina(oficina.getIdOficina())
                .nombreOficina(oficina.getNombreOficina())
                .direccionOficina(oficina.getDireccionOficina())
                .telefonoOficina(oficina.getTelefonoOficina())
                .estadoOficina(oficina.getEstadoOficina())
                .build();
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
