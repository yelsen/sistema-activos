package pe.edu.unasam.activos.modules.sistema.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.enums.EstadoModulo;
import pe.edu.unasam.activos.modules.sistema.domain.ModuloSistema;
import pe.edu.unasam.activos.modules.sistema.dto.*;
import pe.edu.unasam.activos.modules.sistema.repository.ModuloSistemaRepository;
import java.util.ArrayList;

import java.util.List;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModuloSistemaService {

    private final ModuloSistemaRepository moduloRepository;

    /**
     * Listar con paginación
     */
    @Transactional(readOnly = true)
    public Page<ModuloSistemaDTO.Response> listarPaginado(Pageable pageable) {
        log.debug("Listando módulos paginados: {}", pageable);
        return moduloRepository.findAllByOrderByOrdenModuloAsc(pageable)
                .map(this::toSimpleDTO);
    }

    /**
     * Buscar por ID
     */
    @Transactional(readOnly = true)
    public ModuloSistemaDTO.Response buscarPorId(Integer id) {
        log.debug("Buscando módulo con ID: {}", id);
        ModuloSistema modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + id));
        return toSimpleDTO(modulo);
    }

    /**
     * Buscar por ruta
     */
    @Transactional(readOnly = true)
    public ModuloSistemaDTO.Response buscarPorRuta(String ruta) {
        log.debug("Buscando módulo con ruta: {}", ruta);
        ModuloSistema modulo = moduloRepository.findByRutaModulo(ruta)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ruta: " + ruta));
        return toSimpleDTO(modulo);
    }

    /**
     * Buscar con filtros
     */
    @Transactional(readOnly = true)
    public Page<ModuloSistemaDTO.Response> buscarConFiltros(ModuloSistemaDTO.FilterRequest filtro, Pageable pageable) {
        log.debug("Buscando módulos con filtros: {}", filtro);
        return moduloRepository.buscarConFiltros(
                filtro.getNombreModulo(),
                filtro.getEstadoModulo(),
                filtro.getRutaModulo(),
                pageable).map(this::toSimpleDTO);
    }

    /**
     * Obtener para select/combo
     */
    @Transactional(readOnly = true)
    public List<ModuloSistemaDTO.Response> obtenerParaSelect() {
        log.debug("Obteniendo módulos para select");
        return moduloRepository.findForSelect()
                .stream()
                .map(this::toSimpleDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ModuloSistemaDTO.Response crear(ModuloSistemaDTO.Request dto) {
        log.info("Creando nuevo módulo: {}", dto.getNombreModulo());

        // Validaciones
        validarNombreUnico(dto.getNombreModulo(), null);
        if (dto.getRutaModulo() != null && !dto.getRutaModulo().isEmpty()) {
            validarRutaUnica(dto.getRutaModulo(), null);
        }

        // Asignar orden automático si no se especifica
        if (dto.getOrdenModulo() == null) {
            dto.setOrdenModulo(moduloRepository.findMaxOrden() + 1);
        }

        // Validar que el padre exista si se proporciona
        if (dto.getIdModuloPadre() != null) {
            moduloRepository.findById(dto.getIdModuloPadre())
                    .orElseThrow(
                            () -> new RuntimeException("Módulo padre no encontrado con ID: " + dto.getIdModuloPadre()));
        }

        // Crear entidad
        ModuloSistema modulo = ModuloSistema.builder()
                .nombreModulo(dto.getNombreModulo())
                .descripcionModulo(dto.getDescripcionModulo())
                .iconoModulo(dto.getIconoModulo())
                .rutaModulo(dto.getRutaModulo())
                .ordenModulo(dto.getOrdenModulo())
                .estadoModulo(dto.getEstadoModulo())
                .build();

        ModuloSistema guardado = moduloRepository.save(modulo);
        log.info("Módulo creado con ID: {}", guardado.getIdModuloSistemas());

        return toSimpleDTO(guardado);
    }

    /**
     * Actualizar módulo
     */
    @Transactional
    public ModuloSistemaDTO.Response actualizar(ModuloSistemaDTO.Request dto) {
        log.info("Actualizando módulo ID: {}", dto.getIdModuloSistemas());

        // Buscar módulo existente
        ModuloSistema modulo = moduloRepository.findById(dto.getIdModuloSistemas())
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + dto.getIdModuloSistemas()));

        // Validaciones
        validarNombreUnico(dto.getNombreModulo(), dto.getIdModuloSistemas());
        if (dto.getRutaModulo() != null && !dto.getRutaModulo().isEmpty()) {
            validarRutaUnica(dto.getRutaModulo(), dto.getIdModuloSistemas());
        }

        // Actualizar campos
        modulo.setNombreModulo(dto.getNombreModulo());
        modulo.setDescripcionModulo(dto.getDescripcionModulo());
        modulo.setIconoModulo(dto.getIconoModulo());
        modulo.setRutaModulo(dto.getRutaModulo());
        modulo.setOrdenModulo(dto.getOrdenModulo());
        modulo.setEstadoModulo(dto.getEstadoModulo());

        ModuloSistema actualizado = moduloRepository.save(modulo);
        log.info("Módulo actualizado: {}", actualizado.getIdModuloSistemas());

        return toSimpleDTO(actualizado);
    }

    /**
     * Eliminar módulo (soft delete)
     */
    @Transactional
    public void eliminar(Integer id) {
        log.info("Eliminando módulo ID: {}", id);

        ModuloSistema modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + id));

        // Aquí podrías verificar dependencias antes de eliminar
        // Por ejemplo: verificar si tiene permisos asociados

        moduloRepository.delete(modulo); // Ejecuta el soft delete
        log.info("Módulo eliminado: {}", id);
    }

    /**
     * Cambiar estado
     */
    @Transactional
    public ModuloSistemaDTO.Response cambiarEstado(Integer id, EstadoModulo nuevoEstado) {
        log.info("Cambiando estado del módulo ID {} a {}", id, nuevoEstado);

        ModuloSistema modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + id));

        modulo.setEstadoModulo(nuevoEstado);
        ModuloSistema actualizado = moduloRepository.save(modulo);

        return toSimpleDTO(actualizado);
    }

    /**
     * Reordenar módulos
     */
    @Transactional
    public void reordenar(List<Integer> idsOrdenados) {
        log.info("Reordenando {} módulos", idsOrdenados.size());
        List<ModuloSistema> modulosParaActualizar = new ArrayList<>();

        for (int i = 0; i < idsOrdenados.size(); i++) {
            Integer id = idsOrdenados.get(i);
            ModuloSistema modulo = moduloRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + id));
            modulo.setOrdenModulo(i + 1);
            modulosParaActualizar.add(modulo);
        }
        moduloRepository.saveAll(modulosParaActualizar);

        log.info("Reordenamiento completado");
    }

    // ========== VALIDACIONES ==========

    private void validarNombreUnico(String nombre, Integer idExcluir) {
        boolean existe = (idExcluir == null)
                ? moduloRepository.existsByNombreModulo(nombre) // Para creación
                : moduloRepository.existsByNombreModuloAndIdModuloSistemasNot(nombre, idExcluir); // Para actualización

        if (existe) {
            throw new RuntimeException("Ya existe un módulo con el nombre: " + nombre);
        }
    }

    private void validarRutaUnica(String ruta, Integer idExcluir) {
        boolean existe = (idExcluir == null)
                ? moduloRepository.existsByRutaModulo(ruta) // Para creación
                : moduloRepository.existsByRutaModuloAndIdModuloSistemasNot(ruta, idExcluir); // Para actualización

        if (existe) {
            throw new RuntimeException("Ya existe un módulo con la ruta: " + ruta);
        }
    }

    // ========== CONVERSIÓN ==========

    private ModuloSistemaDTO.Response toSimpleDTO(ModuloSistema modulo) {
        return ModuloSistemaDTO.Response.builder()
                .idModuloSistemas(modulo.getIdModuloSistemas())
                .nombreModulo(modulo.getNombreModulo())
                .descripcionModulo(modulo.getDescripcionModulo())
                .iconoModulo(modulo.getIconoModulo())
                .rutaModulo(modulo.getRutaModulo())
                .ordenModulo(modulo.getOrdenModulo())
                .estadoModulo(modulo.getEstadoModulo())
                .estadoModuloTexto(modulo.getEstadoModulo() != null ? modulo.getEstadoModulo().name() : "")
                .createdAt(modulo.getCreatedAt())
                .updatedAt(modulo.getUpdatedAt())
                .build();
    }

    // ========== ESTADÍSTICAS ==========

    @Transactional(readOnly = true)
    public EstadisticasDTO obtenerEstadisticas() {
        long total = moduloRepository.count();
        long activos = moduloRepository.countByEstadoModulo(EstadoModulo.ACTIVO);
        long inactivos = moduloRepository.countByEstadoModulo(EstadoModulo.INACTIVO);

        return new EstadisticasDTO(total, activos, inactivos);
    }

    @Data
    @AllArgsConstructor
    public static class EstadisticasDTO {
        private long total;
        private long activos;
        private long inactivos;
    }
}
