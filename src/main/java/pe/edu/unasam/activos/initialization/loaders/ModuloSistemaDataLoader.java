package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.enums.EstadoModulo;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.ModuloSistema;
import pe.edu.unasam.activos.modules.sistema.repository.ModuloSistemaRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
public class ModuloSistemaDataLoader extends AbstractDataLoader {

    private final ModuloSistemaRepository moduloSistemaRepository;

    @Override
    protected String getLoaderName() {
        return "Módulos del Sistema";
    }

    @Override
    protected boolean shouldLoad() {
        return moduloSistemaRepository.count() == 0;
    }

    @Override
    @Transactional
    protected void loadData() {
        List<ModuloSistema> modulos = new ArrayList<>();

        // ===================== SECCIÓN REPORTES =====================
        modulos.add(crearModulo("Reportes", "Generación de reportes e informes", "ti-chart-bar", "/reportes", 1));

        // ===================== SECCIÓN PRINCIPAL: INVENTARIO =====================
        modulos.add(crearModulo("Activos", "Gestión de activos tecnológicos", "ti-device-laptop", "/activos", 10));
        modulos.add(crearModulo("Equipos", "Gestión de equipos físicos", "ti-cpu", "/equipos", 11));
        modulos.add(crearModulo("Asignacion Activos", "Asignaciones de activos", "ti-user-check", "/activos/asignaciones", 12));
        modulos.add(crearModulo("Categorias", "Categorías de equipos", "ti-tag", "/equipos/categorias", 13));
        modulos.add(crearModulo("Marcas", "Marcas de equipos", "ti-tags", "/equipos/marcas", 14));
        modulos.add(crearModulo("Modelos", "Modelos de equipos", "ti-devices-pc", "/equipos/modelos", 15));
        modulos.add(crearModulo("Especificaciones", "Especificaciones técnicas de equipos", "ti-list-details", "/equipos/especificaciones", 16));
        modulos.add(crearModulo("Categoria Componentes", "Categorías de componentes", "ti-components", "/equipos/categoria-componentes", 17));
        modulos.add(crearModulo("Unidades", "Unidades de medida", "ti-scale", "/equipos/unidades", 18));

        // ===================== SECCIÓN PRINCIPAL: SOFTWARE =====================
        modulos.add(crearModulo("Aplicativos", "Gestión de aplicativos de software", "ti-app-window", "/aplicativos", 20));
        modulos.add(crearModulo("Instalacion Aplicativos", "Instalaciones de aplicativos", "ti-download", "/aplicativos/instalaciones", 21));
        modulos.add(crearModulo("APIS", "APIs e integraciones", "ti-plug", "/apis", 22));
        modulos.add(crearModulo("Licencias", "Gestión de licencias de software", "ti-license", "/licencias", 23));
        modulos.add(crearModulo("Tipo Aplicativos", "Tipos de aplicativos", "ti-apps", "/aplicativos/tipos", 24));
        modulos.add(crearModulo("Tipo Licencias", "Tipos de licencias", "ti-id", "/licencias/tipos", 25));

        // ===================== SECCIÓN INFRAESTRUCTURA =====================
        modulos.add(crearModulo("Servidores", "Gestión de servidores", "ti-server", "/servidores", 30));
        modulos.add(crearModulo("Aplicativos en Servidores", "Aplicativos desplegados en servidores", "ti-app-window", "/servidores/aplicativos", 31));
        modulos.add(crearModulo("Rol Servidores", "Roles de servidores", "ti-shield", "/servidores/roles", 32));
        modulos.add(crearModulo("Recursos Servidores", "Recursos y capacidades de servidores", "ti-cpu", "/servidores/recursos", 33));

        // ===================== SECCIÓN OPERACIONES =====================
        modulos.add(crearModulo("Mantenimientos", "Registro de mantenimientos", "ti-clipboard-list", "/mantenimientos", 40));
        modulos.add(crearModulo("Calendario de Mantenimientos", "Calendario y programación", "ti-calendar-event", "/mantenimientos/calendario", 41));
        modulos.add(crearModulo("Tecnicos", "Gestión de técnicos de mantenimiento", "ti-user-cog", "/mantenimientos/tecnicos", 42));
        modulos.add(crearModulo("Especialidades", "Especialidades técnicas", "ti-star", "/mantenimientos/especialidades", 43));
        modulos.add(crearModulo("Tipos de Mantenimientos", "Tipos de mantenimientos", "ti-category", "/mantenimientos/tipos", 44));

        // ===================== SECCIÓN ORGANIZACIÓN =====================
        modulos.add(crearModulo("Personas", "Gestión de personas", "ti-user", "/personas", 50));
        modulos.add(crearModulo("Responsables", "Responsables de activos", "ti-user-check", "/personas/responsables", 51));
        modulos.add(crearModulo("Oficinas", "Oficinas y ubicaciones físicas", "ti-building", "/ubicaciones/oficinas", 52));
        modulos.add(crearModulo("Departamentos", "Departamentos de la institución", "ti-building-community", "/ubicaciones/departamentos", 53));
        modulos.add(crearModulo("Cargos", "Cargos del personal", "ti-briefcase", "/personas/cargos", 54));
        modulos.add(crearModulo("Tipo Oficinas", "Tipos de oficinas", "ti-office", "/ubicaciones/tipo-oficinas", 55));

        // ===================== SECCIÓN PROVEEDORES =====================
        modulos.add(crearModulo("Proveedores", "Gestión de proveedores", "ti-truck-delivery", "/proveedores", 60));
        modulos.add(crearModulo("Origenes", "Orígenes de adquisición de activos", "ti-building-store", "/proveedores/origenes", 61));

        // ===================== SECCIÓN ADMINISTRACIÓN: SEGURIDAD =====================
        modulos.add(crearModulo("Usuarios", "Usuarios del sistema", "ti-users", "/seguridad/usuarios", 70));
        modulos.add(crearModulo("Roles", "Roles y permisos de usuarios", "ti-user-shield", "/seguridad/roles", 71));
        modulos.add(crearModulo("Permisos", "Permisos del sistema", "ti-key", "/seguridad/permisos", 72));
        modulos.add(crearModulo("Modulos", "Gestión de módulos del sistema", "ti-box-multiple", "/seguridad/modulos", 73));
        modulos.add(crearModulo("Acciones", "Acciones disponibles en el sistema", "ti-actions", "/seguridad/acciones", 74));

        // ===================== SECCIÓN ADMINISTRACIÓN: SISTEMA =====================
        modulos.add(crearModulo("Configuracion", "Configuración general del sistema", "ti-settings", "/sistema/configuracion", 80));
        modulos.add(crearModulo("Auditoria", "Registro de auditoría del sistema", "ti-file-search", "/sistema/auditoria", 81));
        modulos.add(crearModulo("Sesiones", "Sesiones activas de usuarios", "ti-login", "/sistema/sesiones", 82));

        moduloSistemaRepository.saveAll(modulos);
    }

    private ModuloSistema crearModulo(String nombre, String descripcion, String icono, String ruta, int orden) {
        return ModuloSistema.builder()
                .nombreModulo(nombre)
                .descripcionModulo(descripcion)
                .iconoModulo(icono)
                .rutaModulo(ruta)
                .ordenModulo(orden)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build();
    }
}
