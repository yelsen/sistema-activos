package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.sistema.domain.ModuloSistema;
import pe.edu.unasam.activos.modules.sistema.dto.ModuloSistemaDTO;
import pe.edu.unasam.activos.modules.sistema.repository.ModuloSistemaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ModuloSistemaService {

    private final ModuloSistemaRepository moduloRepository;

    @Transactional(readOnly = true)
    public List<ModuloSistemaDTO.Response> getAllModulos() {
        return moduloRepository.findAllByOrderByOrdenModuloAsc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void updateOrden(List<ModuloSistemaDTO.OrdenRequest> ordenRequests) {
        List<ModuloSistema> modulos = moduloRepository.findAll();

        for (ModuloSistemaDTO.OrdenRequest req : ordenRequests) {
            modulos.stream()
                    .filter(m -> m.getIdModuloSistemas().equals(req.getId()))
                    .findFirst()
                    .ifPresent(m -> m.setOrdenModulo(req.getOrden()));
        }
        moduloRepository.saveAll(modulos);
    }

    private ModuloSistemaDTO.Response convertToDto(ModuloSistema modulo) {
        return new ModuloSistemaDTO.Response(
                modulo.getIdModuloSistemas(),
                modulo.getNombreModulo(),
                modulo.getDescripcionModulo(),
                modulo.getIconoModulo(),
                modulo.getEstadoModulo()
        );
    }
}
