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

        // ========== MÓDULOS PRINCIPALES ==========
        modulos.add(crearModulo("Dashboard", "Panel principal del sistema", "ti-layout-dashboard", "/dashboard", 1));
        modulos.add(crearModulo("Reportes", "Generación de reportes e informes", "ti-chart-bar", "/reportes", 2));

        // ========== GESTIÓN PRINCIPAL: ACTIVOS Y EQUIPOS ==========
        modulos.add(crearModulo("Activos", "Gestión de activos tecnológicos", "ti-device-laptop", "/activos", 3));
        modulos.add(crearModulo("Equipos", "Gestión de equipos físicos", "ti-cpu", "/equipos", 4));
        modulos.add(crearModulo("Categorias", "Categorías de equipos", "ti-tag", "/equipos/categorias", 5));
        modulos.add(crearModulo("Marcas", "Marcas de equipos", "ti-tags", "/equipos/marcas", 6));
        modulos.add(crearModulo("Modelos", "Modelos de equipos", "ti-devices-pc", "/equipos/modelos", 7));
        modulos.add(crearModulo("Especificaciones", "Especificaciones técnicas de equipos", "ti-list-details",
                "/equipos/especificaciones", 8));

        // ========== GESTIÓN PRINCIPAL: APLICATIVOS Y LICENCIAS ==========
        modulos.add(
                crearModulo("Aplicativos", "Gestión de aplicativos de software", "ti-app-window", "/aplicativos", 9));
        modulos.add(crearModulo("Tipo Aplicativos", "Tipos de aplicativos", "ti-apps", "/aplicativos/tipos", 10));
        modulos.add(crearModulo("Integraciones de APIs", "APIs externas que usan los sistemas", "ti-plug", "/aplicativos/apis", 11));
        modulos.add(crearModulo("Licencias", "Gestión de licencias de software", "ti-license", "/aplicativos/licencias", 12));
        modulos.add(crearModulo("Tipo Licencias", "Tipos de licencias", "ti-id", "/aplicativos/tipo_licencias", 13));
        modulos.add(crearModulo("Asignaciones", "Asignaciones de licencias a usuarios", "ti-user-check",
                "/aplicativos/asignacion_licencias", 14));

        // ========== GESTIÓN OPERATIVA: MANTENIMIENTOS ==========
        modulos.add(crearModulo("Mantenimientos", "Registro de mantenimientos", "ti-clipboard-list", "/mantenimientos",
                15));
        modulos.add(crearModulo("Tipos de Mantenimientos", "Tipos de mantenimientos", "ti-category",
                "/mantenimientos/tipos", 16));
        modulos.add(crearModulo("Técnicos", "Gestión de técnicos de mantenimiento", "ti-user-cog",
                "/mantenimientos/tecnicos", 17));
        modulos.add(crearModulo("Especialidades", "Especialidades técnicas", "ti-star",
                "/mantenimientos/especialidades", 18));
        modulos.add(crearModulo("Calendario de Mantenimientos", "Calendario y programación", "ti-calendar-event",
                "/mantenimientos/calendario", 19));

        // ========== GESTIÓN ORGANIZACIONAL: ORGANIZACIÓN ==========
        modulos.add(crearModulo("Personal", "Gestión de personal de la institución", "ti-user", "/personas", 20));
        modulos.add(
                crearModulo("Responsables", "Responsables de activos", "ti-user-check", "/personas/responsables", 21));
        modulos.add(crearModulo("Cargos", "Cargos del personal", "ti-briefcase", "/personas/cargos", 22));
        modulos.add(
                crearModulo("Oficinas", "Oficinas y ubicaciones físicas", "ti-building", "/ubicaciones/oficinas", 23));
        modulos.add(crearModulo("Departamentos", "Departamentos de la institución", "ti-building-community",
                "/ubicaciones/departamentos", 24));

        // ========== GESTIÓN ORGANIZACIONAL: PROVEEDORES ==========
        modulos.add(crearModulo("Proveedores", "Gestión de proveedores", "ti-truck-delivery", "/proveedores", 25));
        modulos.add(crearModulo("Orígenes", "Orígenes de adquisición de activos", "ti-building-store",
                "/ubicaciones/origenes", 26));

        // ========== SISTEMA Y SEGURIDAD: SEGURIDAD ==========
        modulos.add(crearModulo("Usuarios", "Usuarios del sistema", "ti-users", "/seguridad/usuarios", 27));
        modulos.add(crearModulo("Roles", "Roles y permisos de usuarios", "ti-user-shield", "/seguridad/roles", 28));
        modulos.add(crearModulo("Permisos", "Permisos del sistema", "ti-key", "/seguridad/permisos", 29));
        modulos.add(crearModulo("Politicas", "Políticas de seguridad", "ti-shield-lock", "/seguridad/politicas", 30));

        // ========== SISTEMA Y SEGURIDAD: SISTEMA ==========
        modulos.add(crearModulo("Auditoria", "Registro de auditoría del sistema", "ti-file-search",
                "/sistema/auditoria", 31));
        modulos.add(crearModulo("Sesiones", "Sesiones activas de usuarios", "ti-login", "/sistema/sesiones", 32));
        modulos.add(crearModulo("Configuracion", "Configuración general del sistema", "ti-settings",
                "/sistema/configuracion", 33));
        modulos.add(
                crearModulo("Modulos", "Gestión de módulos del sistema", "ti-box-multiple", "/sistema/modulos", 34));

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
