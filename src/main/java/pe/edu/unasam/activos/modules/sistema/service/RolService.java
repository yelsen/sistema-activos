package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pe.edu.unasam.activos.common.enums.EstadoRol;
import pe.edu.unasam.activos.common.exception.NotFoundException;
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

@Service
@Transactional
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Page<RolDTO.Response> findAll(String query, EstadoRol estado, Pageable pageable) {
        Specification<Rol> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(query)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nombreRol")),
                        "%" + query.toLowerCase() + "%"));
            }
            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estadoRol"), estado));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Rol> rolesPage = rolRepository.findAll(spec, pageable);

        // Optimización para contar usuarios
        List<Integer> rolIds = rolesPage.getContent().stream()
                .map(Rol::getIdRol)
                .collect(Collectors.toList());

        Map<Integer, Long> userCounts = usuarioRepository.countUsersByRolIds(rolIds).stream()
                .collect(Collectors.toMap(result -> (Integer) result[0], result -> (Long) result[1]));

        // Optimización para contar permisos
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
                .estadoRol(rolRequest.getEstadoRol())
                .build();
        Rol savedRol = rolRepository.save(rol);

        updateRolPermissions(savedRol, rolRequest.getPermisoIds());

        return convertToDtoWithPermissions(savedRol);
    }

    @Transactional
    public RolDTO.Response update(Integer id, RolDTO.Request rolRequest) {
        Rol existingRol = rolRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));

        existingRol.setNombreRol(rolRequest.getNombreRol());
        existingRol.setDescripcionRol(rolRequest.getDescripcionRol());
        existingRol.setNivelAcceso(rolRequest.getNivelAcceso());
        existingRol.setColorRol(rolRequest.getColorRol());
        existingRol.setEstadoRol(rolRequest.getEstadoRol());

        Rol updatedRol = rolRepository.save(existingRol);

        updateRolPermissions(updatedRol, rolRequest.getPermisoIds());

        return convertToDtoWithPermissions(updatedRol);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!rolRepository.existsById(id)) {
            throw new NotFoundException("Rol no encontrado con ID: " + id);
        }
        rolPermisoRepository.deleteByRol_IdRol(id);
        rolRepository.deleteById(id);
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
                .map(this::convertToPermisoResponse)
                .collect(Collectors.toList());
    }

    private void updateRolPermissions(Rol rol, List<Integer> newPermisoIds) {
        // Delete existing permissions for this role
        rolPermisoRepository.deleteByRol_IdRol(rol.getIdRol());

        // Add new permissions
        if (newPermisoIds != null && !newPermisoIds.isEmpty()) {
            List<RolPermiso> rolPermisos = newPermisoIds.stream()
                    .map(permisoId -> {
                        Permiso permiso = permisoRepository.findById(permisoId)
                                .orElseThrow(() -> new NotFoundException("Permiso no encontrado con ID: " + permisoId));
                        return RolPermiso.builder()
                                .rol(rol)
                                .permiso(permiso)
                                .permitido(true)
                                .build();
                    })
                    .collect(Collectors.toList());
            rolPermisoRepository.saveAll(rolPermisos);
        }
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
        RolDTO.Response dto = convertToDtoSimple(rol, usuarioRepository.countByRol(rol), rolPermisoRepository.countByRol(rol.getIdRol()));
        List<RolPermiso> rolPermisos = rolPermisoRepository.findByRol_IdRol(rol.getIdRol());
        if (rolPermisos != null) {
            dto.setPermisos(rolPermisos.stream()
                    .map(rp -> RolDTO.Response.RolPermisoResponse.builder()
                            .permiso(convertToPermisoResponse(rp.getPermiso()))
                            .permitido(rp.isPermitido())
                            .build())
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private PermisoDTO.Response convertToPermisoResponse(Permiso permiso) {
        return PermisoDTO.Response.builder()
                .idPermiso(permiso.getIdPermiso())
                .codigoPermiso(permiso.getCodigoPermiso())
                .nombrePermiso(permiso.getNombrePermiso())
                .descripcionPermiso(permiso.getDescripcionPermiso())
                .moduloSistema(null)
                .accion(null)
                .build();
    }

    private PermisoDTO.Response convertToFullPermisoResponse(Permiso permiso) {
        if (permiso == null) return null;

        var moduloDto = new ModuloSistemaDTO.Response();
        if (permiso.getModuloSistema() != null) {
            moduloDto.setIdModuloSistemas(permiso.getModuloSistema().getIdModuloSistemas());
            moduloDto.setNombreModulo(permiso.getModuloSistema().getNombreModulo());
        }

        var accionDto = new AccionDTO.Response();
        if (permiso.getAccion() != null) {
            accionDto.setNombreAccion(permiso.getAccion().getNombreAccion());
        }

        return PermisoDTO.Response.builder()
                .idPermiso(permiso.getIdPermiso())
                .codigoPermiso(permiso.getCodigoPermiso())
                .nombrePermiso(permiso.getNombrePermiso())
                .moduloSistema(moduloDto)
                .accion(accionDto)
                .build();
    }
}
