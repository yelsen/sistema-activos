package pe.edu.unasam.activos.modules.sistema.service;

import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ConfiguracionSistemaService {
    List<ConfiguracionSistema> findAll();
    Optional<ConfiguracionSistema> findById(Integer id);
    ConfiguracionSistema save(ConfiguracionSistema configuracion);
    void deleteById(Integer id);
    void saveAll(List<ConfiguracionSistema> configuraciones);
    void updateFromMap(Map<String, String> values);
}
