package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pe.edu.unasam.activos.common.enums.EstadoRol;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.common.exception.ReferentialIntegrityException;
import pe.edu.unasam.activos.modules.sistema.domain.Accion;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;
import pe.edu.unasam.activos.modules.sistema.dto.*;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolPermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PermisoService permisoService;

    @Transactional(readOnly = true)
    public Page<RolDTO.Response> findAll(String query, EstadoRol estado, Pageable pageable, String requiredAuthority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasPermission = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredAuthority));

        if (!hasPermission) {
            return Page.empty(pageable);
        }

        Specification<Rol> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(query)) {
                Predicate searchByName = criteriaBuilder.like(criteriaBuilder.lower(root.get("nombreRol")),
                        "%" + query.toLowerCase() + "%");
                try {
                    Integer levelQuery = Integer.parseInt(query);
                    Predicate searchByLevel = criteriaBuilder.equal(root.get("nivelAcceso"), levelQuery);
                    predicates.add(criteriaBuilder.or(searchByName, searchByLevel));
                } catch (NumberFormatException e) {
                    predicates.add(searchByName);
                }
            }

            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estadoRol"), estado));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Rol> rolesPage = rolRepository.findAll(spec, pageable);

        List<Integer> rolIds = rolesPage.getContent().stream()
                .map(Rol::getIdRol)
                .collect(Collectors.toList());

        Map<Integer, Long> userCounts = usuarioRepository.countUsersByRolIds(rolIds).stream()
                .collect(Collectors.toMap(result -> (Integer) result[0], result -> (Long) result[1]));

        Map<Integer, Long> permissionCounts = rolPermisoRepository.countPermissionsByRolIds(rolIds).stream()
                .collect(Collectors.toMap(result -> (Integer) result[0], result -> (Long) result[1]));

        return rolesPage.map(rol -> convertToDtoSimple(rol,
                userCounts.getOrDefault(rol.getIdRol(), 0L),
                permissionCounts.getOrDefault(rol.getIdRol(), 0L)));
    }

    @Transactional(readOnly = true)
    public Optional<RolDTO.Response> findById(Integer id) {
        return rolRepository.findById(id).map(this::convertToDtoWithPermissions);
    }

    @Transactional
    public RolDTO.Response save(RolDTO.Request rolRequest) {

        Rol rol = Rol.builder()
                .nombreRol(rolRequest.getNombreRol())
                .descripcionRol(rolRequest.getDescripcionRol())
                .nivelAcceso(rolRequest.getNivelAcceso())
                .colorRol(rolRequest.getColorRol())
                .estadoRol(EstadoRol.ACTIVO)
                .build();

        Rol savedRol = rolRepository.saveAndFlush(rol);

        // Restaurar la llamada para guardar los permisos
        updateRolPermissionsFromString(savedRol, rolRequest.getPermisosSeleccionados());

        return convertToDtoWithPermissions(savedRol);
    }
    
    @Transactional
    public RolDTO.Response update(Integer id, RolDTO.Request rolRequest) {
        log.info("Actualizando rol con ID: {}", id);

        Rol existingRol = rolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));

        existingRol.setNombreRol(rolRequest.getNombreRol());
        existingRol.setDescripcionRol(rolRequest.getDescripcionRol());
        existingRol.setNivelAcceso(rolRequest.getNivelAcceso());
        existingRol.setColorRol(rolRequest.getColorRol());
        // FIX: Se olvidó establecer el estado del rol, causando que se guarde como NULL.
        existingRol.setEstadoRol(rolRequest.getEstadoRol());
        
        // Restaurar la llamada para actualizar los permisos
        updateRolPermissionsFromString(existingRol, rolRequest.getPermisosSeleccionados());

        Rol updatedRol = rolRepository.save(existingRol);

        return convertToDtoWithPermissions(updatedRol);
    }

    @Transactional
    public void deleteById(Integer id) {
        log.info("Iniciando borrado lógico para el rol ID: {}", id);
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));

        verificarPrecondicionesDeEliminacion(rol);

        // 1. Eliminar físicamente todas las asignaciones de permisos para este rol.
        log.debug("Eliminando todas las asignaciones de permisos para el rol ID: {}", id);
        rolPermisoRepository.deleteByRol_IdRol(id);

        // 2. Realizar el borrado lógico del rol.
        rolRepository.deleteById(id);
        log.info("Borrado lógico del rol ID: {} completado.", id);
    }

    @Transactional
    public void toggleStatus(Integer id) {
        log.info("Cambiando estado para el rol ID: {}", id);
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));

        // Invertir el estado actual
        EstadoRol nuevoEstado = (rol.getEstadoRol() == EstadoRol.ACTIVO) ? EstadoRol.INACTIVO : EstadoRol.ACTIVO;
        rol.setEstadoRol(nuevoEstado);

        rolRepository.save(rol);
        log.info("El estado del rol ID: {} ha sido cambiado a {}", id, nuevoEstado);
    }

    private void verificarPrecondicionesDeEliminacion(Rol rol) {
        // PRE-CONDICIÓN 1: El rol no debe estar asignado a ningún usuario.
        long usuariosConEsteRol = usuarioRepository.countByRol(rol);
        if (usuariosConEsteRol > 0) {
            throw new ReferentialIntegrityException("No se puede eliminar el rol '" + rol.getNombreRol() + "' porque está asignado a " + usuariosConEsteRol + " usuario(s).");
        }

        // FUTURA PRE-CONDICIÓN 2: Podrías añadir más validaciones aquí.
        // Ejemplo: if (rol.esRolDelSistema()) { throw new ... }
    }
    @Transactional(readOnly = true)
    public List<PermisoDTO.Response> getPermisosPorRol(Integer idRol) {
        List<RolPermiso> rolPermisos = rolPermisoRepository.findByRolIdWithPermisos(idRol);
        return rolPermisos.stream()
                .filter(RolPermiso::isPermitido)
                .map(RolPermiso::getPermiso)
                .map(this::convertToFullPermisoResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PermisoDTO.Response> findAllPermissions() {
        return permisoRepository.findAllWithModuloAndAccion().stream()
                .map(this::convertToFullPermisoResponse)
                .collect(Collectors.toList());
    }

    private List<RolPermiso> updateRolPermissionsFromString(Rol rol, List<String> permisosSeleccionados) {
        log.info("Iniciando actualización de permisos para el rol ID: {}. Permisos seleccionados: {}", rol.getIdRol(), permisosSeleccionados != null ? permisosSeleccionados.size() : 0);

        // 1. Obtener todos los RolPermiso existentes para este rol y ponerlos en un mapa para fácil acceso.
        Map<Integer, RolPermiso> permisosActualesMap = rolPermisoRepository.findByRol_IdRol(rol.getIdRol()).stream()
                .collect(Collectors.toMap(rp -> rp.getPermiso().getIdPermiso(), rp -> rp));
        log.debug("El rol tiene {} permisos preexistentes en la tabla de unión.", permisosActualesMap.size());

        // 2. Marcar todos los permisos existentes como NO permitidos inicialmente.
        //    Se actualizarán a 'true' solo si vienen en la lista del formulario.
        permisosActualesMap.values().forEach(rp -> rp.setPermitido(false));

        // 3. Procesar los permisos que vienen del formulario.
        if (permisosSeleccionados != null && !permisosSeleccionados.isEmpty()) {
            for (String permisoStr : permisosSeleccionados) {
                try {
                    String[] ids = permisoStr.split("-");
                    if (ids.length != 2) {
                        log.warn("Formato de permiso inválido, se omitirá: {}", permisoStr);
                        continue;
                    }
                    Integer idModulo = Integer.parseInt(ids[0]);
                    Integer idAccion = Integer.parseInt(ids[1]);

                    // 4. Buscar el permiso. Si no existe, se crea en el acto.
                    Permiso permiso = permisoService.findOrCreatePermiso(idModulo, idAccion);

                    // FIX: Asegurarse de que el permiso esté gestionado por la sesión actual
                    // volviéndolo a buscar si es necesario, aunque findOrCreatePermiso ya debería devolverlo gestionado.
                    Permiso managedPermiso = permisoRepository.findById(permiso.getIdPermiso()).get();

                    RolPermiso rolPermiso = permisosActualesMap.get(managedPermiso.getIdPermiso());

                    if (rolPermiso != null) {
                        // Si ya existía la relación, simplemente la marcamos como permitida.
                        rolPermiso.setPermitido(true);
                    } else {
                        // Si no existía, creamos una nueva relación y la añadimos al mapa.
                        RolPermiso nuevoRolPermiso = RolPermiso.builder()
                                .rol(rol)
                                .permiso(managedPermiso)
                                .permitido(true)
                                .build();
                        permisosActualesMap.put(managedPermiso.getIdPermiso(), nuevoRolPermiso);
                    }
                } catch (NumberFormatException e) {
                    log.error("Error al parsear IDs del permiso: {}", permisoStr, e);
                }
            }
        }

        // 5. Guardar todos los cambios (actualizaciones y nuevas inserciones).
        List<RolPermiso> rolPermisosAGuardar = new ArrayList<>(permisosActualesMap.values());
        if (!rolPermisosAGuardar.isEmpty()) {
            rolPermisoRepository.saveAll(rolPermisosAGuardar);
        }

        log.info("Actualización de permisos para el rol ID: {} completada.", rol.getIdRol());

        return rolPermisosAGuardar;
    }

    private RolDTO.Response convertToDtoSimple(Rol rol, long userCount, long permissionCount) {
        return RolDTO.Response.builder()
                .idRol(rol.getIdRol())
                .nombreRol(rol.getNombreRol())
                .descripcionRol(rol.getDescripcionRol())
                .nivelAcceso(rol.getNivelAcceso())
                .colorRol(rol.getColorRol())
                .estadoRol(rol.getEstadoRol())
                .usuariosCount(userCount)
                .permisosCount(permissionCount)
                .build();
    }
    
    private RolDTO.Response convertToDtoWithPermissions(Rol rol) {
        // Usamos la consulta que carga todas las relaciones para evitar LazyInitializationException
        List<RolPermiso> rolPermisos = rolPermisoRepository.findByRolIdWithPermisos(rol.getIdRol());

        RolDTO.Response dto = convertToDtoSimple(rol,
                usuarioRepository.countByRol(rol),
                rolPermisos.stream().filter(RolPermiso::isPermitido).count());

        if (!rolPermisos.isEmpty()) {
            dto.setPermisos(rolPermisos.stream()
                    .map(rp -> RolDTO.Response.RolPermisoResponse.builder()
                            .permiso(convertToFullPermisoResponse(rp.getPermiso()))
                            .permitido(rp.isPermitido())
                            .build())
                    .collect(Collectors.toList()));

            // Agrupar permisos para la vista de detalles
            Map<ModuloSistemaDTO.Response, List<AccionDTO.Response>> groupedPermissions = rolPermisos.stream()
                    .filter(RolPermiso::isPermitido)
                    .map(RolPermiso::getPermiso)
                    .filter(p -> p != null && p.getModuloSistema() != null && p.getAccion() != null)
                    .collect(Collectors.groupingBy(this::convertToFullModuloResponse,
                            Collectors.mapping(p -> convertToFullAccionResponse(p.getAccion()), Collectors.toList())));

            List<RolDTO.Response.PermisoAgrupadoResponse> permisosAgrupados = groupedPermissions.entrySet().stream()
                    .map(entry -> RolDTO.Response.PermisoAgrupadoResponse.builder()
                            .modulo(entry.getKey())
                            .acciones(entry.getValue())
                            .build())
                    .sorted((p1, p2) -> Integer.compare(p1.getModulo().getOrdenModulo(), p2.getModulo().getOrdenModulo()))
                    .collect(Collectors.toList());
            dto.setPermisosAgrupados(permisosAgrupados);
        }
        return dto;
    }
    private PermisoDTO.Response convertToFullPermisoResponse(Permiso permiso) {
        if (permiso == null)
            return null;

        ModuloSistemaDTO.Response moduloDto = null;
        if (permiso.getModuloSistema() != null) {
            moduloDto = ModuloSistemaDTO.Response.builder()
                    .idModuloSistemas(permiso.getModuloSistema().getIdModuloSistemas())
                    .nombreModulo(permiso.getModuloSistema().getNombreModulo())
                    .descripcionModulo(permiso.getModuloSistema().getDescripcionModulo())
                    .iconoModulo(permiso.getModuloSistema().getIconoModulo())
                    .estadoModulo(permiso.getModuloSistema().getEstadoModulo())
                    .build();
        }

        AccionDTO.Response accionDto = null;
        if (permiso.getAccion() != null) {
            accionDto = AccionDTO.Response.builder()
                    .idAccion(permiso.getAccion().getIdAccion())
                    .nombreAccion(permiso.getAccion().getNombreAccion())
                    .codigoAccion(permiso.getAccion().getCodigoAccion())
                    .descripcionAccion(permiso.getAccion().getDescripcionAccion())
                    .build();
        }

        return PermisoDTO.Response.builder()
                .idPermiso(permiso.getIdPermiso())
                .codigoPermiso(permiso.getCodigoPermiso())
                .nombrePermiso(permiso.getNombrePermiso())
                .moduloSistema(moduloDto)
                .descripcionPermiso(permiso.getDescripcionPermiso())
                .accion(accionDto)
                .build();
    }

    private ModuloSistemaDTO.Response convertToFullModuloResponse(Permiso permiso) {
        if (permiso == null || permiso.getModuloSistema() == null) return null;
        var modulo = permiso.getModuloSistema();
        return ModuloSistemaDTO.Response.builder()
                .idModuloSistemas(modulo.getIdModuloSistemas())
                .nombreModulo(modulo.getNombreModulo())
                .descripcionModulo(modulo.getDescripcionModulo())
                .iconoModulo(modulo.getIconoModulo())
                .estadoModulo(modulo.getEstadoModulo())
                .ordenModulo(modulo.getOrdenModulo())
                .build();
    }

    private AccionDTO.Response convertToFullAccionResponse(Accion accion) {
        if (accion == null) return null;
        return AccionDTO.Response.builder()
                .idAccion(accion.getIdAccion())
                .nombreAccion(accion.getNombreAccion())
                .codigoAccion(accion.getCodigoAccion())
                .descripcionAccion(accion.getDescripcionAccion())
                .build();
    }
}
