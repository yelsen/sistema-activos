package pe.edu.unasam.activos.modules.ubicaciones.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.ubicaciones.domain.TipoOficina;
import pe.edu.unasam.activos.modules.ubicaciones.dto.TipoOficinaDTO;
import pe.edu.unasam.activos.modules.ubicaciones.repository.TipoOficinaRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoOficinaService {

    private final TipoOficinaRepository tipoOficinaRepository;

    @Transactional(readOnly = true)
    public Page<TipoOficinaDTO.Response> getAllTipoOficinas(String query, Pageable pageable) {
        Page<TipoOficina> page;
        if (query != null && !query.isBlank()) {
            page = tipoOficinaRepository.findByTipoOficinaContainingIgnoreCase(query.trim(), pageable);
        } else {
            page = tipoOficinaRepository.findAll(pageable);
        }
        return page.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public TipoOficinaDTO.Response getById(Integer id) {
        TipoOficina tipo = tipoOficinaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de oficina no encontrado con ID: " + id));
        return toDto(tipo);
    }

    public TipoOficinaDTO.Response create(TipoOficinaDTO.Request request) {
        String nombre = trimToNull(request.getTipoOficina());
        if (nombre == null) {
            throw new BusinessException("El tipo de oficina es obligatorio.");
        }
        if (tipoOficinaRepository.existsByTipoOficina(nombre)) {
            throw new BusinessException("Ya existe un tipo de oficina con el nombre '" + nombre + "'.");
        }
        TipoOficina tipo = TipoOficina.builder()
                .tipoOficina(nombre)
                .build();
        return toDto(tipoOficinaRepository.save(tipo));
    }

    public TipoOficinaDTO.Response update(Integer id, TipoOficinaDTO.Request request) {
        TipoOficina tipo = tipoOficinaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de oficina no encontrado con ID: " + id));

        String nombre = trimToNull(request.getTipoOficina());
        if (nombre == null) {
            throw new BusinessException("El tipo de oficina es obligatorio.");
        }

        Optional<TipoOficina> existente = tipoOficinaRepository.findByTipoOficina(nombre);
        if (existente.isPresent() && !existente.get().getIdTipoOficina().equals(id)) {
            throw new BusinessException("Ya existe un tipo de oficina con el nombre '" + nombre + "'.");
        }

        tipo.setTipoOficina(nombre);
        return toDto(tipoOficinaRepository.save(tipo));
    }

    public void delete(Integer id) {
        tipoOficinaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de oficina no encontrado con ID: " + id));
        tipoOficinaRepository.deleteById(id);
    }

    private TipoOficinaDTO.Response toDto(TipoOficina t) {
        return new TipoOficinaDTO.Response(t.getIdTipoOficina(), t.getTipoOficina());
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
