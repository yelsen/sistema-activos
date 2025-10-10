package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.Accion;
import pe.edu.unasam.activos.modules.sistema.domain.ModuloSistema;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;
import pe.edu.unasam.activos.modules.sistema.repository.AccionRepository; 
import pe.edu.unasam.activos.modules.sistema.repository.ModuloSistemaRepository;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(5)
@RequiredArgsConstructor
public class PermisoDataLoader extends AbstractDataLoader {

        private final PermisoRepository permisoRepository;
        private final ModuloSistemaRepository moduloSistemaRepository;
        private final AccionRepository accionRepository;

        @Override
        protected String getLoaderName() {
                return "Permisos del Sistema";
        }

        @Override
        protected boolean shouldLoad() {
                return permisoRepository.count() == 0;
        }

        @Override
        protected void loadData() {
                List<ModuloSistema> modulos = moduloSistemaRepository.findAllByOrderByOrdenModuloAsc();
                // Usar un Map para búsqueda rápida O(1) en lugar de O(n) con stream().filter()
                Map<String, Accion> accionesMap = accionRepository.findAll().stream()
                                .collect(Collectors.toMap(Accion::getCodigoAccion, accion -> accion));

                List<Permiso> permisos = new ArrayList<>();

                // Definir acciones estándar
                List<String> crudCompleto = Arrays.asList("LEER", "VER", "CREAR", "EDITAR", "ELIMINAR");

                // Mapeo de Módulos a sus submódulos y acciones
                // La clave DEBE COINCIDIR EXACTAMENTE con `nombreModulo` de ModuloSistemaDataLoader
                Map<String, List<String>> modulosConSubmodulos = new LinkedHashMap<>();
                modulosConSubmodulos.put("Activos", Arrays.asList("Activos"));
                modulosConSubmodulos.put("Equipos", Arrays.asList("Equipos", "Categorias", "Marcas", "Modelos", "Especificaciones"));
                modulosConSubmodulos.put("Aplicativos", Arrays.asList("Aplicativos", "Tipo_Aplicativos", "APIs"));
                modulosConSubmodulos.put("Licencias", Arrays.asList("Licencias", "Tipo_Licencias", "Asignaciones"));
                modulosConSubmodulos.put("Mantenimientos", Arrays.asList("Mantenimientos", "Tipo_Mantenimientos", "Tecnicos", "Especialidades", "Calendario"));
                modulosConSubmodulos.put("Organización", Arrays.asList("Personas", "Responsables", "Cargos", "Oficinas", "Departamentos"));
                modulosConSubmodulos.put("Proveedores", Arrays.asList("Proveedores", "Origenes"));
                modulosConSubmodulos.put("Seguridad", Arrays.asList("Usuarios", "Roles", "Permisos", "Politicas", "Auditoria", "Sesiones"));
                modulosConSubmodulos.put("Configuración", Arrays.asList("Configuracion", "Modulos"));

                for (ModuloSistema modulo : modulos) {
                        String nombreModulo = modulo.getNombreModulo();

                        // Permiso general de ACCESO al módulo
                        crearPermisoSimple(permisos, modulo, accionesMap.get("ACCEDER"), "Acceder a " + nombreModulo, "Permite el acceso al módulo de " + nombreModulo);

                        if (nombreModulo.equals("Dashboard")) {
                                // Ya tiene el de ACCEDER, es suficiente.
                        } else if (nombreModulo.equals("Reportes")) {
                                crearPermisoSimple(permisos, modulo, accionesMap.get("GENERAR"), "Generar Reportes", "Permite generar reportes del sistema");
                        } else {
                                // Busca el módulo por su nombre exacto en el mapa.
                                List<String> submodulos = modulosConSubmodulos.get(nombreModulo);

                                if (!submodulos.isEmpty()) {
                                        for (String submodulo : submodulos) {
                                                // Para la mayoría de submódulos, creamos permisos CRUD
                                                crearPermisosCRUD(permisos, modulo, accionesMap, submodulo, crudCompleto);
                                        }
                                } else {
                                        // Si no hay submódulos definidos, creamos CRUD para el módulo principal
                                        crearPermisosCRUD(permisos, modulo, accionesMap, nombreModulo, crudCompleto);
                                }
                        }
                }

                permisoRepository.saveAll(permisos);
        }

        private void crearPermisosCRUD(List<Permiso> permisos, ModuloSistema modulo, Map<String, Accion> accionesMap, String nombreEntidad, List<String> acciones) {
                String entidadCodigo = generarCodigoModulo(nombreEntidad);
                String entidadNombre = capitalizar(nombreEntidad.replace("_", " "));

                for (String codigoAccion : acciones) {
                        Accion accion = accionesMap.get(codigoAccion);
                        if (accion != null) {
                                String nombrePermiso;
                                String descripcionPermiso;
                                String nombreAccionLower = accion.getNombreAccion().toLowerCase();

                                switch (codigoAccion) {
                                        case "LEER":
                                                nombrePermiso = "Listar " + entidadNombre;
                                                descripcionPermiso = "Permite ver la lista de " + entidadNombre.toLowerCase();
                                                break;
                                        case "VER":
                                                nombrePermiso = "Ver detalle de " + entidadNombre;
                                                descripcionPermiso = "Permite ver el detalle de un/a " + entidadNombre.toLowerCase();
                                                break;
                                        default:
                                                nombrePermiso = accion.getNombreAccion() + " " + entidadNombre;
                                                descripcionPermiso = "Permite " + nombreAccionLower + " en " + entidadNombre.toLowerCase();
                                                break;
                                }
                                permisos.add(crearPermiso(entidadCodigo + "_" + codigoAccion, nombrePermiso, descripcionPermiso, modulo, accion));
                        }
                }
        }

        private void crearPermisoSimple(List<Permiso> permisos, ModuloSistema modulo, Accion accion, String nombre,
                        String descripcion) {
                if (accion != null) {
                        String codigo = generarCodigoModulo(modulo.getNombreModulo()) + "_" + accion.getCodigoAccion();
                        permisos.add(crearPermiso(codigo, nombre, descripcion, modulo, accion));
                }
        }

        private String capitalizar(String str) {
                if (str == null || str.isEmpty()) {
                        return str;
                }
                return str.substring(0, 1).toUpperCase() + str.substring(1);
        }

        private String generarCodigoModulo(String nombreModulo) {
                return nombreModulo.toUpperCase().replace(" ", "_")
                        .replace("Á", "A").replace("É", "E").replace("Í", "I")
                        .replace("Ó", "O").replace("Ú", "U")
                        .replace("/", "_");
        }

        private Permiso crearPermiso(String codigo, String nombre, String descripcion,
                        ModuloSistema modulo, Accion accion) {
                return Permiso.builder()
                                .codigoPermiso(codigo)
                                .nombrePermiso(nombre)
                                .descripcionPermiso(descripcion)
                                .moduloSistema(modulo)
                                .accion(accion)
                                .build();
        }
}
