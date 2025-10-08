package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;
import pe.edu.unasam.activos.modules.sistema.repository.ConfiguracionSistemaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfiguracionSistemaServiceImpl implements ConfiguracionSistemaService {

    private final ConfiguracionSistemaRepository configuracionSistemaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ConfiguracionSistema> findAll() {
        return configuracionSistemaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfiguracionSistema> findById(Integer id) {
        return configuracionSistemaRepository.findById(id);
    }

    @Override
    public ConfiguracionSistema save(ConfiguracionSistema configuracion) {
        return configuracionSistemaRepository.save(configuracion);
    }

    @Override
    public void deleteById(Integer id) {
        configuracionSistemaRepository.deleteById(id);
    }

    @Override
    public void saveAll(List<ConfiguracionSistema> configuracion) {
        configuracionSistemaRepository.saveAll(configuracion);
    }

    @Override
    @Transactional
    public void updateFromMap(Map<String, String> values) {
        List<ConfiguracionSistema> configsToUpdate = configuracionSistemaRepository
                .findByClaveConfigIn(values.keySet());

        for (ConfiguracionSistema config : configsToUpdate) {
            if (values.containsKey(config.getClaveConfig())) {
                config.setValorConfig(values.get(config.getClaveConfig()));
            }
        }

        configuracionSistemaRepository.saveAll(configsToUpdate);
    }
}
