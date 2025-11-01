package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pe.edu.unasam.activos.common.enums.TipoServidor;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.servidores.domain.RolServidor;
import pe.edu.unasam.activos.modules.servidores.domain.Servidor;
import pe.edu.unasam.activos.modules.servidores.repository.RolServidorRepository;
import pe.edu.unasam.activos.modules.servidores.repository.ServidorRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(27)
@RequiredArgsConstructor
public class ServidoresDataLoader extends AbstractDataLoader {

    private final ServidorRepository servidorRepository;
    private final RolServidorRepository rolServidorRepository;

    @Override
    protected String getLoaderName() {
        return "Servidores";
    }

    @Override
    protected boolean shouldLoad() {
        return servidorRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        RolServidor webServer = rolServidorRepository.findByRolServidor("Web Server").orElseThrow();
        RolServidor dbServer = rolServidorRepository.findByRolServidor("Database Server").orElseThrow();
        RolServidor appServer = rolServidorRepository.findByRolServidor("Application Server").orElseThrow();
        RolServidor fileServer = rolServidorRepository.findByRolServidor("File Server").orElseThrow();
        RolServidor mailServer = rolServidorRepository.findByRolServidor("Mail Server").orElseThrow();
        RolServidor domainController = rolServidorRepository.findByRolServidor("Domain Controller").orElseThrow();
        RolServidor dnsServer = rolServidorRepository.findByRolServidor("DNS Server").orElseThrow();
        RolServidor backupServer = rolServidorRepository.findByRolServidor("Backup Server").orElseThrow();
        RolServidor virtualizationHost = rolServidorRepository.findByRolServidor("Virtualization Host").orElseThrow();
        RolServidor firewall = rolServidorRepository.findByRolServidor("Firewall").orElseThrow();

        List<Servidor> servidores = new ArrayList<>();

        // Servidores Web
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-WEB-SIGA")
                .hosting("On-Premise")
                .direccionIp("192.168.10.10")
                .puertoPrincipal(443)
                .usuarioAdmin("admin_siga")
                .tipoServidor(TipoServidor.FISICO)
                .rolServidor(webServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-WEB-PORTAL")
                .hosting("On-Premise")
                .direccionIp("192.168.10.11")
                .puertoPrincipal(443)
                .usuarioAdmin("admin_web")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(webServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-WEB-AULA-VIRTUAL")
                .hosting("Cloud")
                .direccionIp("192.168.10.12")
                .puertoPrincipal(443)
                .usuarioAdmin("admin_moodle")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(webServer)
                .build());

        // Servidores de Base de Datos
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-DB-PRINCIPAL")
                .hosting("On-Premise")
                .direccionIp("192.168.10.20")
                .puertoPrincipal(3306)
                .usuarioAdmin("dba_unasam")
                .tipoServidor(TipoServidor.FISICO)
                .rolServidor(dbServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-DB-ACADEMICO")
                .hosting("On-Premise")
                .direccionIp("192.168.10.21")
                .puertoPrincipal(5432)
                .usuarioAdmin("dba_academico")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(dbServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-DB-FINANCIERO")
                .hosting("On-Premise")
                .direccionIp("192.168.10.22")
                .puertoPrincipal(1433)
                .usuarioAdmin("dba_finanzas")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(dbServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-DB-REPLICA")
                .hosting("On-Premise")
                .direccionIp("192.168.10.23")
                .puertoPrincipal(3306)
                .usuarioAdmin("dba_replica")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(dbServer)
                .build());

        // Servidores de Aplicaciones
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-APP-SIAF")
                .hosting("On-Premise")
                .direccionIp("192.168.10.30")
                .puertoPrincipal(8080)
                .usuarioAdmin("admin_siaf")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(appServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-APP-RRHH")
                .hosting("On-Premise")
                .direccionIp("192.168.10.31")
                .puertoPrincipal(8080)
                .usuarioAdmin("admin_rrhh")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(appServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-APP-TRAMITE")
                .hosting("On-Premise")
                .direccionIp("192.168.10.32")
                .puertoPrincipal(8443)
                .usuarioAdmin("admin_tramite")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(appServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-APP-BIBLIOTECA")
                .hosting("On-Premise")
                .direccionIp("192.168.10.33")
                .puertoPrincipal(8080)
                .usuarioAdmin("admin_biblioteca")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(appServer)
                .build());

        // Servidor de Archivos
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-FILE-PRINCIPAL")
                .hosting("On-Premise")
                .direccionIp("192.168.10.40")
                .puertoPrincipal(445)
                .usuarioAdmin("admin_files")
                .tipoServidor(TipoServidor.FISICO)
                .rolServidor(fileServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-FILE-ACADEMICO")
                .hosting("On-Premise")
                .direccionIp("192.168.10.41")
                .puertoPrincipal(445)
                .usuarioAdmin("admin_acad_files")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(fileServer)
                .build());

        // Servidor de Correo
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-MAIL-EXCHANGE")
                .hosting("On-Premise")
                .direccionIp("192.168.10.50")
                .puertoPrincipal(25)
                .usuarioAdmin("admin_exchange")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(mailServer)
                .build());

        // Controladores de Dominio
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-DC-PRIMARY")
                .hosting("On-Premise")
                .direccionIp("192.168.10.60")
                .puertoPrincipal(389)
                .usuarioAdmin("administrator")
                .tipoServidor(TipoServidor.FISICO)
                .rolServidor(domainController)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-DC-SECONDARY")
                .hosting("On-Premise")
                .direccionIp("192.168.10.61")
                .puertoPrincipal(389)
                .usuarioAdmin("administrator")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(domainController)
                .build());

        // Servidores DNS
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-DNS-PRIMARY")
                .hosting("On-Premise")
                .direccionIp("192.168.10.70")
                .puertoPrincipal(53)
                .usuarioAdmin("admin_dns")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(dnsServer)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-DNS-SECONDARY")
                .hosting("On-Premise")
                .direccionIp("192.168.10.71")
                .puertoPrincipal(53)
                .usuarioAdmin("admin_dns")
                .tipoServidor(TipoServidor.VIRTUAL)
                .rolServidor(dnsServer)
                .build());

        // Servidor de Backup
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-BACKUP-VEEAM")
                .hosting("On-Premise")
                .direccionIp("192.168.10.80")
                .puertoPrincipal(9392)
                .usuarioAdmin("admin_backup")
                .tipoServidor(TipoServidor.FISICO)
                .rolServidor(backupServer)
                .build());

        // Hosts de Virtualización
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-VMHOST-01")
                .hosting("On-Premise")
                .direccionIp("192.168.10.90")
                .puertoPrincipal(443)
                .usuarioAdmin("root")
                .tipoServidor(TipoServidor.FISICO)
                .rolServidor(virtualizationHost)
                .build());

        servidores.add(Servidor.builder()
                .nombreServidor("SRV-VMHOST-02")
                .hosting("On-Premise")
                .direccionIp("192.168.10.91")
                .puertoPrincipal(443)
                .usuarioAdmin("root")
                .tipoServidor(TipoServidor.FISICO)
                .rolServidor(virtualizationHost)
                .build());

        // Firewall
        servidores.add(Servidor.builder()
                .nombreServidor("SRV-FW-PERIMETRAL")
                .hosting("On-Premise")
                .direccionIp("192.168.10.1")
                .puertoPrincipal(443)
                .usuarioAdmin("admin_fw")
                .tipoServidor(TipoServidor.APPLIANCE)
                .rolServidor(firewall)
                .build());

        servidorRepository.saveAll(servidores);
    }
}
