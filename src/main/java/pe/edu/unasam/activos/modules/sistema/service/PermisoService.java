package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;
import pe.edu.unasam.activos.modules.sistema.dto.*;
import pe.edu.unasam.activos.modules.sistema.repository.AccionRepository;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolPermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import pe.edu.unasam.activos.modules.sistema.dto.AccionDTO;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PermisoService {

    private final UsuarioRepository usuarioRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final PermisoRepository permisoRepository;
    private final AccionRepository accionRepository;

    public List<PermisoDTO.Response> getAllPermisos() {
        return permisoRepository.findAllWithModuloAndAccion().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Map<String, List<PermisoDTO.Response>> getAllPermisosGroupedByModulo() {
        return getAllPermisos().stream()
                .collect(Collectors.groupingBy(p -> p.getModuloSistema().getNombreModulo()));
    }

    public Map<ModuloSistemaDTO.Response, Map<String, PermisoDTO.Response>> getPermisosAsTabla() {
        Map<ModuloSistemaDTO.Response, Map<String, PermisoDTO.Response>> tabla = new java.util.LinkedHashMap<>(); // LinkedHashMap para mantener el orden
        List<PermisoDTO.Response> todosLosPermisos = this.getAllPermisos();

        for (PermisoDTO.Response permiso : todosLosPermisos) {
            ModuloSistemaDTO.Response modulo = permiso.getModuloSistema();
            String codigoAccion = permiso.getAccion().getCodigoAccion().toLowerCase();
            tabla.computeIfAbsent(modulo, k -> new java.util.HashMap<>()).put(codigoAccion, permiso);
        }

        return tabla;
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<AccionDTO.Response> getAllAcciones() {
        return accionRepository.findAll().stream()
                .map(accion -> new AccionDTO.Response(accion.getIdAccion(), accion.getNombreAccion(), accion.getCodigoAccion(), accion.getDescripcionAccion()))
                .collect(Collectors.toList());
    }

    public boolean usuarioTienePermiso(Integer usuarioId, String codigoPermiso) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> {
                    if (usuario.getRol() == null)
                        return false;
                    List<RolPermiso> permisosDelRol = rolPermisoRepository.findByRol_IdRol(usuario.getRol().getIdRol());
                    return permisosDelRol.stream()
                            .anyMatch(rp -> rp.isPermitido()
                                    && rp.getPermiso().getCodigoPermiso().equalsIgnoreCase(codigoPermiso));
                }).orElse(false);
    }

    public List<RolPermiso> getPermisosPorUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .map(usuario -> rolPermisoRepository.findByRolIdWithPermisos(usuario.getRol().getIdRol()))
                .orElse(Collections.emptyList());
    }

    private PermisoDTO.Response convertToDto(Permiso permiso) {
        ModuloSistemaDTO.Response moduloDto = null;
        if (permiso.getModuloSistema() != null) {
            moduloDto = new ModuloSistemaDTO.Response(
                    permiso.getModuloSistema().getIdModuloSistemas(),
                    permiso.getModuloSistema().getNombreModulo(),
                    permiso.getModuloSistema().getDescripcionModulo(),
                    permiso.getModuloSistema().getIconoModulo(),
                    permiso.getModuloSistema().getEstadoModulo());
        }

        AccionDTO.Response accionDto = null;
        if (permiso.getAccion() != null) {
            accionDto = new AccionDTO.Response(
                    permiso.getAccion().getIdAccion(),
                    permiso.getAccion().getNombreAccion(),
                    permiso.getAccion().getCodigoAccion(),
                    permiso.getAccion().getDescripcionAccion());
        }

        return PermisoDTO.Response.builder()
                .idPermiso(permiso.getIdPermiso())
                .codigoPermiso(permiso.getCodigoPermiso())
                .nombrePermiso(permiso.getNombrePermiso())
                .descripcionPermiso(permiso.getDescripcionPermiso())
                .moduloSistema(moduloDto)
                .accion(accionDto)
                .build();
    }
}
