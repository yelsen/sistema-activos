package pe.edu.unasam.activos.modules.ubicaciones.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private OficinaDTO.Response toDto(Oficina oficina) {
        return new OficinaDTO.Response(oficina.getIdOficina(), oficina.getNombreOficina());
    }
}
