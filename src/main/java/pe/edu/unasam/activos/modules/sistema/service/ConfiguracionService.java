package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;
import pe.edu.unasam.activos.modules.sistema.dto.ConfiguracionDTO;
import pe.edu.unasam.activos.modules.sistema.repository.ConfiguracionSistemaRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfiguracionService {

    private final ConfiguracionSistemaRepository configuracionRepository;

    @Transactional(readOnly = true)
    public Page<ConfiguracionDTO.Response> getAll(Pageable pageable) {
        log.debug("Listando configuraciones paginadas: {}", pageable);
        return configuracionRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public ConfiguracionDTO.Response getById(Integer id) {
        log.debug("Buscando configuración con ID: {}", id);
        return configuracionRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NotFoundException("Configuración no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ConfiguracionDTO.Response> search(ConfiguracionDTO.FilterRequest filter, Pageable pageable) {
        log.debug("Buscando configuraciones con filtros: {}", filter);
        return configuracionRepository.searchByFilters(
                filter.getClaveConfig(),
                filter.getCategoriaConfig(),
                pageable
        ).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ConfiguracionDTO.Response> getAllAsList() {
        log.debug("Obteniendo todas las configuraciones como lista");
        return configuracionRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ConfiguracionDTO.Response create(ConfiguracionDTO.Request request) {
        log.info("Creando nueva configuración: {}", request.getClaveConfig());
        validateUniqueKey(request.getClaveConfig(), null);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        ConfiguracionSistema config = ConfiguracionSistema.builder()
                .claveConfig(request.getClaveConfig())
                .valorConfig(request.getValorConfig())
                .descripcionConfig(request.getDescripcionConfig())
                .tipoDato(request.getTipoDato())
                .categoriaConfig(request.getCategoriaConfig())
                .usuarioModificacion(username)
                .build();

        ConfiguracionSistema savedConfig = configuracionRepository.save(config);
        log.info("Configuración creada con ID: {}", savedConfig.getIdConfiguracionSistema());
        return toDTO(savedConfig);
    }

    @Transactional
    public ConfiguracionDTO.Response update(Integer id, ConfiguracionDTO.Request request) {
        log.info("Actualizando configuración ID: {}", id);
        ConfiguracionSistema config = configuracionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Configuración no encontrada con ID: " + id));

        validateUniqueKey(request.getClaveConfig(), id);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        config.setClaveConfig(request.getClaveConfig());
        config.setValorConfig(request.getValorConfig());
        config.setDescripcionConfig(request.getDescripcionConfig());
        config.setTipoDato(request.getTipoDato());
        config.setCategoriaConfig(request.getCategoriaConfig());
        config.setUsuarioModificacion(username);

        ConfiguracionSistema updatedConfig = configuracionRepository.save(config);
        log.info("Configuración actualizada: {}", updatedConfig.getIdConfiguracionSistema());
        return toDTO(updatedConfig);
    }

    @Transactional
    public void updateFromMap(Map<String, String> values) {
        log.info("Actualizando {} configuraciones desde el mapa.", values.size());
        List<ConfiguracionSistema> configsToUpdate = configuracionRepository.findByClaveConfigIn(values.keySet());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        for (ConfiguracionSistema config : configsToUpdate) {
            if (values.containsKey(config.getClaveConfig())) {
                config.setValorConfig(values.get(config.getClaveConfig()));
                config.setUsuarioModificacion(username);
            }
        }

        configuracionRepository.saveAll(configsToUpdate);
        log.info("Actualización masiva completada.");
    }

    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando configuración ID: {}", id);
        if (!configuracionRepository.existsById(id)) {
            throw new NotFoundException("Configuración no encontrada con ID: " + id);
        }
        configuracionRepository.deleteById(id);
        log.info("Configuración eliminada: {}", id);
    }

    private void validateUniqueKey(String key, Integer excludeId) {
        boolean exists = (excludeId == null)
                ? configuracionRepository.existsByClaveConfig(key)
                : configuracionRepository.existsByClaveConfigAndIdConfiguracionSistemaNot(key, excludeId);

        if (exists) {
            throw new RuntimeException("La clave de configuración '" + key + "' ya existe.");
        }
    }

    private ConfiguracionDTO.Response toDTO(ConfiguracionSistema config) {
        return ConfiguracionDTO.Response.builder()
                .idConfiguracionSistema(config.getIdConfiguracionSistema())
                .claveConfig(config.getClaveConfig())
                .valorConfig(config.getValorConfig())
                .descripcionConfig(config.getDescripcionConfig())
                .tipoDato(config.getTipoDato())
                .categoriaConfig(config.getCategoriaConfig())
                .usuarioModificacion(config.getUsuarioModificacion())
                .build();
    }
}
