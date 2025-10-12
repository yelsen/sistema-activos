package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolPermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;

import java.util.*;

@Component
@Order(5)
@RequiredArgsConstructor
public class RolPermisoDataLoader extends AbstractDataLoader {

    private final RolPermisoRepository rolPermisoRepository;
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;

    @Override
    protected String getLoaderName() {
        return "Asignación de Permisos a Roles";
    }

    @Override
    protected boolean shouldLoad() {
        return rolPermisoRepository.count() == 0;
    }

    @Override
    @Transactional
    protected void loadData() {
        Rol adminGeneral = rolRepository.findByNombreRolUnrestricted("ADMIN_GENERAL")
                .orElseThrow(() -> new RuntimeException(
                        "Rol ADMIN_GENERAL no encontrado. Asegúrese que RolDataLoader se ejecute primero."));
        Rol editor = rolRepository.findByNombreRolUnrestricted("EDITOR")
                .orElseThrow(() -> new RuntimeException("Rol EDITOR no encontrado."));
        Rol usuarioConsulta = rolRepository.findByNombreRolUnrestricted("USUARIO_CONSULTA")
                .orElseThrow(() -> new RuntimeException("Rol USUARIO_CONSULTA no encontrado."));

        List<RolPermiso> rolPermisosParaGuardar = new ArrayList<>();

        // --- ASIGNACIÓN DE PERMISOS ---

        List<Permiso> todosLosPermisos = permisoRepository.findAllWithModuloAndAccion();

        // ADMIN_GENERAL: Todos los permisos
        for (Permiso permiso : todosLosPermisos) {
            rolPermisosParaGuardar.add(crearRolPermiso(adminGeneral, permiso, true));
        }

        // EDITOR: Todos los permisos excepto eliminar y gestionar módulos sensibles.
        Set<String> modulosProhibidosEditor = new HashSet<>(Arrays.asList("Seguridad", "Configuración"));
        for (Permiso p : todosLosPermisos) {
            boolean esModuloProhibido = modulosProhibidosEditor.contains(p.getModuloSistema().getNombreModulo());
            boolean esAccionEliminar = p.getAccion().getCodigoAccion().equals("ELIMINAR");
            boolean esAccionLectura = Arrays.asList("LEER", "VER", "ACCEDER").contains(p.getAccion().getCodigoAccion());

            // Si el módulo NO es prohibido Y la acción NO es eliminar, se permite.
            if (!esModuloProhibido && !esAccionEliminar) {
                 rolPermisosParaGuardar.add(crearRolPermiso(editor, p, true));
            }
            // Si el módulo ES prohibido, solo se permiten acciones de lectura.
            else if (esModuloProhibido && esAccionLectura) {
                 rolPermisosParaGuardar.add(crearRolPermiso(editor, p, true));
            }
        }

        // USUARIO_CONSULTA: Solo permisos de lectura, visualización y acceso a módulos.
        Set<String> accionesConsulta = new HashSet<>(Arrays.asList("LEER", "VER", "ACCEDER", "GENERAR"));
        for (Permiso p : todosLosPermisos) {
            boolean esAccionConsulta = accionesConsulta.contains(p.getAccion().getCodigoAccion());
            // Para GENERAR, solo permitirlo en el módulo de Reportes.
            if (p.getAccion().getCodigoAccion().equals("GENERAR")) {
                if (p.getModuloSistema().getNombreModulo().equals("Reportes")) {
                     rolPermisosParaGuardar.add(crearRolPermiso(usuarioConsulta, p, true));
                }
            } else if (esAccionConsulta) {
                 rolPermisosParaGuardar.add(crearRolPermiso(usuarioConsulta, p, true));
            }
        }

        // Usamos una consulta nativa para insertar los datos, evitando problemas de "detached entity".
        // Esto es mucho más robusto para la carga inicial.
        for (RolPermiso rp : rolPermisosParaGuardar) {
            rolPermisoRepository.insertRolPermiso(rp.getRol().getIdRol(), rp.getPermiso().getIdPermiso(), rp.isPermitido());
        }
    }

    private RolPermiso crearRolPermiso(Rol rol, Permiso permiso, boolean permitido) {
        return RolPermiso.builder()
                .rol(rol)
                .permiso(permiso)
                .permitido(permitido)
                .build();
    }
}