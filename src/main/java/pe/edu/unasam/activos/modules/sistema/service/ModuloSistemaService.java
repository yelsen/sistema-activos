package pe.edu.unasam.activos.modules.sistema.service;

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
import java.text.Normalizer;

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

    @Transactional
    public ModuloSistemaDTO.Response crear(ModuloSistemaDTO.Request dto) {
        log.info("Creando nuevo módulo: {}", dto.getNombreModulo());

        // Validaciones
        validarNombreUnico(dto.getNombreModulo(), null);
        // Ruta: si no se proporciona, generar desde el nombre y asegurar unicidad
        if (dto.getRutaModulo() == null || dto.getRutaModulo().isBlank()) {
            String baseRuta = buildRutaFromNombre(dto.getNombreModulo());
            String uniqueRuta = ensureUniqueRutaForCreate(baseRuta);
            dto.setRutaModulo(uniqueRuta);
        } else {
            validarRutaUnica(dto.getRutaModulo(), null);
        }

        // Estado por defecto: ACTIVO (fallback por si no llega desde el cliente)
        if (dto.getEstadoModulo() == null) {
            dto.setEstadoModulo(EstadoModulo.ACTIVO);
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
        // Si viene una ruta no vacía, validar unicidad para actualización
        if (dto.getRutaModulo() != null && !dto.getRutaModulo().trim().isBlank()) {
            validarRutaUnica(dto.getRutaModulo().trim(), dto.getIdModuloSistemas());
        }

        // Actualizar campos
        modulo.setNombreModulo(dto.getNombreModulo());
        modulo.setDescripcionModulo(dto.getDescripcionModulo());
        modulo.setIconoModulo(dto.getIconoModulo());
        // Actualizar ruta solo si viene una nueva no vacía
        if (dto.getRutaModulo() != null && !dto.getRutaModulo().trim().isBlank()) {
            modulo.setRutaModulo(dto.getRutaModulo().trim());
        }
        // Conservar el orden actual si no llega en el DTO
        if (dto.getOrdenModulo() != null) {
            modulo.setOrdenModulo(dto.getOrdenModulo());
        }
        if (dto.getEstadoModulo() != null) {
            modulo.setEstadoModulo(dto.getEstadoModulo());
        }

        ModuloSistema actualizado = moduloRepository.save(modulo);
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

    // ========== UTILIDADES DE RUTA ==========
    private String buildRutaFromNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            return "/modulo";
        String slug = slugify(nombre);
        return slug.startsWith("/") ? slug : ("/" + slug);
    }

    private String ensureUniqueRutaForCreate(String baseRuta) {
        String ruta = baseRuta;
        int sufijo = 2;
        while (moduloRepository.existsByRutaModulo(ruta)) {
            ruta = baseRuta + "-" + sufijo++;
        }
        return ruta;
    }

    private String slugify(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String lower = normalized.toLowerCase();
        // Reemplazar cualquier cosa que no sea a-z, 0-9 con guiones
        String slug = lower.replaceAll("[^a-z0-9]+", "-");
        // Quitar guiones al inicio/fin
        slug = slug.replaceAll("^-+", "").replaceAll("-+$", "");
        return slug.isBlank() ? "modulo" : slug;
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
}
