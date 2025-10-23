package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.sistema.domain.Accion;
import pe.edu.unasam.activos.modules.sistema.domain.ModuloSistema;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;
import pe.edu.unasam.activos.modules.sistema.dto.*;
import pe.edu.unasam.activos.modules.sistema.repository.AccionRepository;
import pe.edu.unasam.activos.modules.sistema.repository.ModuloSistemaRepository;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolPermisoRepository;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PermisoService {

    private final UsuarioRepository usuarioRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final PermisoRepository permisoRepository;
    private final AccionRepository accionRepository;
    private final ModuloSistemaRepository moduloSistemaRepository;

    @Cacheable("permisos")
    public List<PermisoDTO.Response> getAllPermisos() {
        return permisoRepository.findAllWithModuloAndAccion().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Cacheable("permisosAgrupados")
    public Map<String, List<PermisoDTO.Response>> getAllPermisosGroupedByModulo() {
        return getAllPermisos().stream()
                .collect(Collectors.groupingBy(p -> p.getModuloSistema().getNombreModulo()));
    }

    @Cacheable("permisosTabla")
    public Map<ModuloSistemaDTO.Response, Map<String, PermisoDTO.Response>> getPermisosAsTabla() {
        Map<ModuloSistemaDTO.Response, Map<String, PermisoDTO.Response>> tabla = new java.util.LinkedHashMap<>();
        List<PermisoDTO.Response> todosLosPermisos = this.getAllPermisos();

        for (PermisoDTO.Response permiso : todosLosPermisos) {
            ModuloSistemaDTO.Response modulo = permiso.getModuloSistema();
            if (permiso.getAccion() == null || permiso.getAccion().getCodigoAccion() == null)
                continue;
            String codigoAccion = permiso.getAccion().getCodigoAccion().toLowerCase();
            tabla.computeIfAbsent(modulo, k -> new java.util.HashMap<>()).put(codigoAccion, permiso);
        }

        return tabla;
    }

    @Cacheable("acciones")
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<AccionDTO.Response> getAllAcciones() {
        return accionRepository.findAll().stream()
                .map(accion -> new AccionDTO.Response(
                        accion.getIdAccion(),
                        accion.getNombreAccion(),
                        accion.getCodigoAccion(),
                        accion.getDescripcionAccion()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Permiso findOrCreatePermiso(Integer idModulo, Integer idAccion) {

        List<Permiso> permisosExistentes = permisoRepository.findByModuloAndAccion(idModulo, idAccion);

        if (!permisosExistentes.isEmpty()) {
            return permisosExistentes.get(0);
        }

        ModuloSistema modulo = moduloSistemaRepository.findById(idModulo)
                .orElseThrow(() -> new NotFoundException("Módulo no encontrado con ID: " + idModulo));

        Accion accion = accionRepository.findById(idAccion)
                .orElseThrow(() -> new NotFoundException("Acción no encontrada con ID: " + idAccion));

        String codigoPermiso = generarCodigoPermiso(modulo.getNombreModulo(), accion.getCodigoAccion());
        String nombrePermiso = generarNombrePermiso(modulo.getNombreModulo(), accion.getNombreAccion());

        Permiso nuevoPermiso = Permiso.builder()
                .codigoPermiso(codigoPermiso)
                .nombrePermiso(nombrePermiso)
                .descripcionPermiso("Permiso autogenerado para la acción '" + accion.getNombreAccion()
                        + "' en el módulo '" + modulo.getNombreModulo() + "'.")
                .moduloSistema(modulo)
                .accion(accion)
                .build();

        Permiso permisoGuardado = permisoRepository.saveAndFlush(nuevoPermiso);
        
        return permisoGuardado;
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
            moduloDto = ModuloSistemaDTO.Response.builder()
                    .idModuloSistemas(permiso.getModuloSistema().getIdModuloSistemas())
                    .nombreModulo(permiso.getModuloSistema().getNombreModulo())
                    .descripcionModulo(permiso.getModuloSistema().getDescripcionModulo())
                    .iconoModulo(permiso.getModuloSistema().getIconoModulo())
                    .rutaModulo(permiso.getModuloSistema().getRutaModulo())
                    .ordenModulo(permiso.getModuloSistema().getOrdenModulo())
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
                .descripcionPermiso(permiso.getDescripcionPermiso())
                .moduloSistema(moduloDto)
                .accion(accionDto)
                .build();
    }

    public String generarCodigoPermiso(String nombreModulo, String codigoAccion) {
        String codigoModulo = nombreModulo.toUpperCase().replace(" ", "_")
                .replace("Á", "A").replace("É", "E").replace("Í", "I")
                .replace("Ó", "O").replace("Ú", "U")
                .replace("/", "_");
        return codigoModulo + "_" + codigoAccion.toUpperCase();
    }

    public String generarNombrePermiso(String nombreModulo, String nombreAccion) {
        return capitalizar(nombreAccion) + " " + capitalizar(nombreModulo.replace("_", " "));
    }

    public String generarDescripcionPermiso(String nombreModulo, String nombreAccion) {
        return "Permite " + nombreAccion.toLowerCase() + " en "
                + capitalizar(nombreModulo.replace("_", " ")).toLowerCase();
    }

    private String capitalizar(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
