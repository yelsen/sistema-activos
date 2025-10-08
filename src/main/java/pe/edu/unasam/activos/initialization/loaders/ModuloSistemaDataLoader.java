package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import pe.edu.unasam.activos.common.enums.EstadoModulo;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.ModuloSistema;
import pe.edu.unasam.activos.modules.sistema.repository.ModuloSistemaRepository;

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
    protected void loadData() {
        ModuloSistema[] modulos = {
            ModuloSistema.builder()
                .nombreModulo("Dashboard")
                .descripcionModulo("Panel principal del sistema")
                .iconoModulo("fas fa-tachometer-alt")
                .rutaModulo("/dashboard")
                .ordenModulo(1)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),
            
            ModuloSistema.builder()
                .nombreModulo("Activos")
                .descripcionModulo("Gestión de activos tecnológicos")
                .iconoModulo("fas fa-archive")
                .rutaModulo("/activos")
                .ordenModulo(2)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),
            
            ModuloSistema.builder()
                .nombreModulo("Equipos")
                .descripcionModulo("Gestión de equipos, categorías, marcas y modelos")
                .iconoModulo("fas fa-laptop-code")
                .rutaModulo("/equipos")
                .ordenModulo(3)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),
            
            ModuloSistema.builder()
                .nombreModulo("Aplicativos")
                .descripcionModulo("Gestión de software e instalaciones")
                .iconoModulo("fab fa-windows")
                .rutaModulo("/aplicativos")
                .ordenModulo(4)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),
            
            ModuloSistema.builder()
                .nombreModulo("Licencias")
                .descripcionModulo("Gestión de licencias de software")
                .iconoModulo("fas fa-id-badge")
                .rutaModulo("/licencias")
                .ordenModulo(5)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),
            
            ModuloSistema.builder()
                .nombreModulo("Mantenimientos")
                .descripcionModulo("Programación y gestión de mantenimientos")
                .iconoModulo("fas fa-tools")
                .rutaModulo("/mantenimientos")
                .ordenModulo(6)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),

            ModuloSistema.builder()
                .nombreModulo("Organización")
                .descripcionModulo("Gestiona personal, responsables, cargos, oficinas y departamentos")
                .iconoModulo("fas fa-sitemap")
                .rutaModulo("/organizacion")
                .ordenModulo(7)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),
            
            ModuloSistema.builder()
                .nombreModulo("Proveedores")
                .descripcionModulo("Administración de proveedores y adquisiciones")
                .iconoModulo("fas fa-truck")
                .rutaModulo("/proveedores")
                .ordenModulo(8)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),

            ModuloSistema.builder()
                .nombreModulo("Reportes")
                .descripcionModulo("Generación de reportes del sistema")
                .iconoModulo("fas fa-chart-bar")
                .rutaModulo("/reportes")
                .ordenModulo(9)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),

            ModuloSistema.builder()
                .nombreModulo("Seguridad")
                .descripcionModulo("Administración de usuarios, roles y permisos")
                .iconoModulo("fas fa-shield-alt")
                .rutaModulo("/seguridad")
                .ordenModulo(10)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build(),

             ModuloSistema.builder()
                .nombreModulo("Configuración")
                .descripcionModulo("Administra la configuración general, auditoría y módulos")
                .iconoModulo("fas fa-cogs")
                .rutaModulo("/configuracion")
                .ordenModulo(11)
                .estadoModulo(EstadoModulo.ACTIVO)
                .build()
        };
        
        moduloSistemaRepository.saveAll(Arrays.asList(modulos));
    }
}