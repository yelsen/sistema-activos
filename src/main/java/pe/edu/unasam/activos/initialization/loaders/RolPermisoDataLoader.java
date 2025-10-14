package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolPermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;

import java.util.*;

@Component
@Order(6)
@RequiredArgsConstructor
public class RolPermisoDataLoader extends AbstractDataLoader {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RolPermisoRepository rolPermisoRepository;

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
        Rol adminRol = rolRepository.findByNombreRol("ADMIN_GENERAL")
                .orElseThrow(() -> new RuntimeException(
                        "Rol ADMIN_GENERAL no encontrado. Asegúrate de que RolDataLoader se ejecute antes."));
        Rol editorRol = rolRepository.findByNombreRol("EDITOR")
                .orElseThrow(() -> new RuntimeException("Rol EDITOR no encontrado."));
        Rol consultaRol = rolRepository.findByNombreRol("USUARIO_CONSULTA")
                .orElseThrow(() -> new RuntimeException("Rol USUARIO_CONSULTA no encontrado."));

        List<Permiso> todosLosPermisos = permisoRepository.findAllWithModuloAndAccion();

        Set<String> accionesConsulta = Set.of("LEER", "VER", "ACCEDER");
        Set<String> accionesEditor = Set.of("LEER", "VER", "ACCEDER", "CREAR", "EDITAR", "GENERAR");

        Set<String> modulosRestringidosEditor = Set.of(
                "Usuarios", "Roles", "Permisos", "Politicas",
                "Auditoria", "Sesiones", "Configuracion", "Modulos");

        for (Permiso permiso : todosLosPermisos) {
            String codigoAccion = permiso.getAccion().getCodigoAccion();
            String nombreModulo = permiso.getModuloSistema().getNombreModulo();

            // Rol ADMIN_GENERAL: tiene todos los permisos
            rolPermisoRepository.insertRolPermiso(adminRol.getIdRol(), permiso.getIdPermiso(), true); // Siempre
                                                                                                      // permitido

            // Rol EDITOR: tiene permisos de edición, pero no en módulos restringidos
            boolean permitidoParaEditor = accionesEditor.contains(codigoAccion)
                    && !modulosRestringidosEditor.contains(nombreModulo);
            if (permitidoParaEditor) {
                rolPermisoRepository.insertRolPermiso(editorRol.getIdRol(), permiso.getIdPermiso(), true);
            }

            // Rol USUARIO_CONSULTA: solo tiene permisos de lectura
            boolean permitidoParaConsulta = accionesConsulta.contains(codigoAccion);
            if (permitidoParaConsulta) {
                rolPermisoRepository.insertRolPermiso(consultaRol.getIdRol(), permiso.getIdPermiso(), true);
            }
        }
    }
}
