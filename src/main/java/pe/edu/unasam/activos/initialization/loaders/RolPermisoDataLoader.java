package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolPermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Order(7)
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
        Rol adminGeneral = rolRepository.findByNombreRol("ADMIN_GENERAL")
                .orElseThrow(() -> new RuntimeException(
                        "Rol ADMIN_GENERAL no encontrado. Asegúrese que RolDataLoader se ejecute primero."));
        Rol editor = rolRepository.findByNombreRol("EDITOR")
                .orElseThrow(() -> new RuntimeException("Rol EDITOR no encontrado."));
        Rol usuarioConsulta = rolRepository.findByNombreRol("USUARIO_CONSULTA")
                .orElseThrow(() -> new RuntimeException("Rol USUARIO_CONSULTA no encontrado."));

        // Usar el nuevo método para evitar LazyInitializationException
        List<Permiso> todosPermisos = permisoRepository.findAllWithModuloAndAccion();
        List<RolPermiso> rolPermisos = new ArrayList<>();

        // ADMIN_GENERAL: Todos los permisos
        for (Permiso permiso : todosPermisos) {
            rolPermisos.add(crearRolPermiso(adminGeneral, permiso, true));
        }

        // EDITOR: Permisos de gestión pero sin eliminar y sin acceso a configuración/seguridad sensible
        List<String> modulosProhibidosEditor = Arrays.asList("Seguridad", "Sistema");
        List<Permiso> permisosEditor = todosPermisos.stream()
                .filter(p -> !modulosProhibidosEditor.contains(p.getModuloSistema().getNombreModulo()))
                .filter(p -> !p.getAccion().getCodigoAccion().equals("ELIMINAR"))
                .collect(Collectors.toList());
        for (Permiso permiso : permisosEditor) {
            rolPermisos.add(crearRolPermiso(editor, permiso, true));
        }
        // Damos acceso de solo lectura a los módulos prohibidos para que pueda verlos
        List<Permiso> permisosLecturaEditor = todosPermisos.stream()
                .filter(p -> modulosProhibidosEditor.contains(p.getModuloSistema().getNombreModulo()))
                .filter(p -> Arrays.asList("LEER", "VER", "ACCEDER").contains(p.getAccion().getCodigoAccion()))
                .collect(Collectors.toList());
        for (Permiso permiso : permisosLecturaEditor) {
            rolPermisos.add(crearRolPermiso(editor, permiso, true));
        }

        // USUARIO_CONSULTA: Solo permisos de lectura, visualización y acceso a módulos.
        List<String> accionesConsulta = Arrays.asList("LEER", "VER", "ACCEDER");
        List<Permiso> permisosConsulta = todosPermisos.stream()
                .filter(p -> accionesConsulta.contains(p.getAccion().getCodigoAccion()))
                .collect(Collectors.toList());
        for (Permiso permiso : permisosConsulta) {
            rolPermisos.add(crearRolPermiso(usuarioConsulta, permiso, true));
        }
        // Permiso especial para generar reportes
        todosPermisos.stream()
                .filter(p -> p.getModuloSistema().getNombreModulo().equals("Reportes") && p.getAccion().getCodigoAccion().equals("GENERAR"))
                .findFirst()
                .ifPresent(p -> rolPermisos.add(crearRolPermiso(usuarioConsulta, p, true)));

        // Filtrar duplicados antes de guardar. Un permiso por rol.
        List<RolPermiso> rolPermisosUnicos = rolPermisos.stream()
                .filter(distinctByKey(rp -> rp.getRol().getIdRol() + "-" + rp.getPermiso().getIdPermiso()))
                .collect(Collectors.toList());

        rolPermisoRepository.saveAll(rolPermisosUnicos);
    }

    private static <T> Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private RolPermiso crearRolPermiso(Rol rol, Permiso permiso, boolean permitido) {
        return RolPermiso.builder()
                .rol(rol)
                .permiso(permiso)
                .permitido(permitido)
                .build();
    }
}