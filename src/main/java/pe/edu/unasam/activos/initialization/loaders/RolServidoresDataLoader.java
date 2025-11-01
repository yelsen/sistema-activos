package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pe.edu.unasam.activos.common.enums.NivelCriticidad;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.servidores.domain.RolServidor;
import pe.edu.unasam.activos.modules.servidores.repository.RolServidorRepository;

import java.util.Arrays;

@Component
@Order(16)
@RequiredArgsConstructor
public class RolServidoresDataLoader extends AbstractDataLoader {

    private final RolServidorRepository rolServidorRepository;

    @Override
    protected String getLoaderName() {
        return "Roles de Servidor";
    }

    @Override
    protected boolean shouldLoad() {
        return rolServidorRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        RolServidor[] roles = {
                RolServidor.builder()
                        .rolServidor("Web Server")
                        .descripcionRol("Servidor que aloja aplicaciones web y sitios institucionales")
                        .nivelCriticidad(NivelCriticidad.ALTO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Database Server")
                        .descripcionRol("Servidor dedicado a bases de datos empresariales")
                        .nivelCriticidad(NivelCriticidad.CRITICO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Application Server")
                        .descripcionRol("Servidor de aplicaciones y lógica de negocio")
                        .nivelCriticidad(NivelCriticidad.ALTO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("File Server")
                        .descripcionRol("Almacenamiento centralizado de archivos institucionales")
                        .nivelCriticidad(NivelCriticidad.MEDIO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Mail Server")
                        .descripcionRol("Servidor de correo electrónico institucional")
                        .nivelCriticidad(NivelCriticidad.ALTO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Domain Controller")
                        .descripcionRol("Controlador de dominio Active Directory")
                        .nivelCriticidad(NivelCriticidad.CRITICO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("DNS Server")
                        .descripcionRol("Servidor de resolución de nombres de dominio")
                        .nivelCriticidad(NivelCriticidad.MEDIO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("DHCP Server")
                        .descripcionRol("Asignación automática de direcciones IP")
                        .nivelCriticidad(NivelCriticidad.MEDIO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Proxy Server")
                        .descripcionRol("Control y filtrado de acceso a internet")
                        .nivelCriticidad(NivelCriticidad.ALTO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Firewall")
                        .descripcionRol("Protección perimetral de red y seguridad")
                        .nivelCriticidad(NivelCriticidad.CRITICO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Backup Server")
                        .descripcionRol("Respaldo y recuperación de información crítica")
                        .nivelCriticidad(NivelCriticidad.CRITICO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Monitoring Server")
                        .descripcionRol("Supervisión de infraestructura y servicios")
                        .nivelCriticidad(NivelCriticidad.MEDIO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Virtualization Host")
                        .descripcionRol("Host de virtualización de servidores")
                        .nivelCriticidad(NivelCriticidad.CRITICO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Load Balancer")
                        .descripcionRol("Balanceo de carga entre servidores")
                        .nivelCriticidad(NivelCriticidad.ALTO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("VPN Server")
                        .descripcionRol("Acceso remoto seguro a la red institucional")
                        .nivelCriticidad(NivelCriticidad.ALTO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Print Server")
                        .descripcionRol("Gestión centralizada de impresoras")
                        .nivelCriticidad(NivelCriticidad.BAJO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Development Server")
                        .descripcionRol("Entorno de desarrollo y pruebas")
                        .nivelCriticidad(NivelCriticidad.BAJO)
                        .build(),
                RolServidor.builder()
                        .rolServidor("Testing Server")
                        .descripcionRol("Servidor para pruebas de aplicaciones")
                        .nivelCriticidad(NivelCriticidad.BAJO)
                        .build()
        };
        rolServidorRepository.saveAll(Arrays.asList(roles));
    }
}
