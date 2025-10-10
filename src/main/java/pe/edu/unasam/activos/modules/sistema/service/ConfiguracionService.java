package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;
import pe.edu.unasam.activos.modules.sistema.dto.ConfiguracionDTO;
import pe.edu.unasam.activos.modules.sistema.repository.ConfiguracionSistemaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfiguracionService {

    private final ConfiguracionSistemaRepository configuracionRepository;

    @Transactional(readOnly = true)
    public List<ConfiguracionDTO.Response> getAll() {
        return configuracionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ConfiguracionDTO.Response> getById(Integer id) {
        return configuracionRepository.findById(id).map(this::convertToDto);
    }

    public void updateFromMap(Map<String, String> values) {
        List<ConfiguracionSistema> configsToUpdate = configuracionRepository.findByClaveConfigIn(values.keySet());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        for (ConfiguracionSistema config : configsToUpdate) {
            if (values.containsKey(config.getClaveConfig())) {
                config.setValorConfig(values.get(config.getClaveConfig()));
                config.setUsuarioModificacion(username);
            }
        }
        configuracionRepository.saveAll(configsToUpdate);
    }

    private ConfiguracionDTO.Response convertToDto(ConfiguracionSistema config) {
        return new ConfiguracionDTO.Response(
                config.getIdConfiguracionSistema(),
                config.getClaveConfig(),
                config.getValorConfig(),
                config.getDescripcionConfig(),
                config.getTipoDato(),
                config.getCategoriaConfig(),
                config.getUsuarioModificacion()
        );
    }
}
