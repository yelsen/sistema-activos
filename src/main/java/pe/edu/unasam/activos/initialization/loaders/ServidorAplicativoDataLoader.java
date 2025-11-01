package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pe.edu.unasam.activos.common.enums.EstadoInstalacion;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.aplicativos.domain.Aplicativo;
import pe.edu.unasam.activos.modules.aplicativos.domain.ServidorAplicativo;
import pe.edu.unasam.activos.modules.aplicativos.repository.AplicativoRepository;
import pe.edu.unasam.activos.modules.aplicativos.repository.ServidorAplicativoRepository;
import pe.edu.unasam.activos.modules.servidores.domain.Servidor;
import pe.edu.unasam.activos.modules.servidores.repository.ServidorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

@Component
@Order(30)
@RequiredArgsConstructor
public class ServidorAplicativoDataLoader extends AbstractDataLoader {

    private final ServidorAplicativoRepository servidorAplicativoRepository;
    private final ServidorRepository servidorRepository;
    private final AplicativoRepository aplicativoRepository;

    @Override
    protected String getLoaderName() {
        return "Aplicativos en Servidores";
    }

    @Override
    protected boolean shouldLoad() {
        return servidorAplicativoRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<ServidorAplicativo> instalaciones = new ArrayList<>();

        // Buscar servidores
        Optional<Servidor> srvWebSigaOpt = servidorRepository.findByNombreServidor("SRV-WEB-SIGA");
        Optional<Servidor> srvWebPortalOpt = servidorRepository.findByNombreServidor("SRV-WEB-PORTAL");
        Optional<Servidor> srvWebAulaVirtualOpt = servidorRepository.findByNombreServidor("SRV-WEB-AULA-VIRTUAL");
        Optional<Servidor> srvDbPrincipalOpt = servidorRepository.findByNombreServidor("SRV-DB-PRINCIPAL");
        Optional<Servidor> srvDbAcademicoOpt = servidorRepository.findByNombreServidor("SRV-DB-ACADEMICO");
        Optional<Servidor> srvDbFinancieroOpt = servidorRepository.findByNombreServidor("SRV-DB-FINANCIERO");
        Optional<Servidor> srvAppSiafOpt = servidorRepository.findByNombreServidor("SRV-APP-SIAF");
        Optional<Servidor> srvAppRrhhOpt = servidorRepository.findByNombreServidor("SRV-APP-RRHH");
        Optional<Servidor> srvAppTramiteOpt = servidorRepository.findByNombreServidor("SRV-APP-TRAMITE");
        Optional<Servidor> srvAppBibliotecaOpt = servidorRepository.findByNombreServidor("SRV-APP-BIBLIOTECA");
        Optional<Servidor> srvDcPrimaryOpt = servidorRepository.findByNombreServidor("SRV-DC-PRIMARY");
        Optional<Servidor> srvDcSecondaryOpt = servidorRepository.findByNombreServidor("SRV-DC-SECONDARY");

        // Buscar aplicativos
        Optional<Aplicativo> sigaUnasamOpt = aplicativoRepository.findByNombreAplicativo("SIGA UNASAM");
        Optional<Aplicativo> aulaVirtualOpt = aplicativoRepository.findByNombreAplicativo("Aula Virtual UNASAM");
        Optional<Aplicativo> bibliotecaOpt = aplicativoRepository.findByNombreAplicativo("Sistema de Biblioteca");
        Optional<Aplicativo> admisionOpt = aplicativoRepository.findByNombreAplicativo("Portal de Admisión");
        Optional<Aplicativo> siafOpt = aplicativoRepository.findByNombreAplicativo("SIAF Universidad");
        Optional<Aplicativo> rrhhOpt = aplicativoRepository.findByNombreAplicativo("Sistema de Recursos Humanos");
        Optional<Aplicativo> tramiteOpt = aplicativoRepository.findByNombreAplicativo("Sistema de Trámite Documentario");
        Optional<Aplicativo> logisticaOpt = aplicativoRepository.findByNombreAplicativo("Sistema de Logística");
        Optional<Aplicativo> mesaPartesOpt = aplicativoRepository.findByNombreAplicativo("Sistema de Mesa de Partes");
        Optional<Aplicativo> mysqlOpt = aplicativoRepository.findByNombreAplicativo("MySQL");
        Optional<Aplicativo> postgresqlOpt = aplicativoRepository.findByNombreAplicativo("PostgreSQL");
        Optional<Aplicativo> sqlServerOpt = aplicativoRepository.findByNombreAplicativo("SQL Server 2022");
        Optional<Aplicativo> windowsServerOpt = aplicativoRepository.findByNombreAplicativo("Windows Server 2022");
        Optional<Aplicativo> ubuntuServerOpt = aplicativoRepository.findByNombreAplicativo("Ubuntu Server");

        // Helper to add installation if both server and app are present
        BiConsumer<Optional<Servidor>, Optional<Aplicativo>> addInstalacion = (servidorOpt, aplicativoOpt) -> {
            if (servidorOpt.isPresent() && aplicativoOpt.isPresent()) {
                instalaciones.add(ServidorAplicativo.builder()
                        .servidor(servidorOpt.get())
                        .aplicativo(aplicativoOpt.get())
                        .estadoInstalacion(EstadoInstalacion.PRODUCCION)
                        .build());
            }
        };

        // SRV-WEB-SIGA - Servidor web del SIGA
        addInstalacion.accept(srvWebSigaOpt, sigaUnasamOpt);
        addInstalacion.accept(srvWebSigaOpt, ubuntuServerOpt);

        // SRV-WEB-PORTAL - Portal institucional
        addInstalacion.accept(srvWebPortalOpt, admisionOpt);
        addInstalacion.accept(srvWebPortalOpt, ubuntuServerOpt);

        // SRV-WEB-AULA-VIRTUAL - Moodle
        addInstalacion.accept(srvWebAulaVirtualOpt, aulaVirtualOpt);
        addInstalacion.accept(srvWebAulaVirtualOpt, ubuntuServerOpt);

        // SRV-DB-PRINCIPAL - Base de datos MySQL principal
        addInstalacion.accept(srvDbPrincipalOpt, mysqlOpt);
        addInstalacion.accept(srvDbPrincipalOpt, ubuntuServerOpt);

        // SRV-DB-ACADEMICO - PostgreSQL para sistema académico
        addInstalacion.accept(srvDbAcademicoOpt, postgresqlOpt);
        addInstalacion.accept(srvDbAcademicoOpt, ubuntuServerOpt);

        // SRV-DB-FINANCIERO - SQL Server para SIAF
        addInstalacion.accept(srvDbFinancieroOpt, sqlServerOpt);
        addInstalacion.accept(srvDbFinancieroOpt, windowsServerOpt);

        // SRV-APP-SIAF - Aplicativo de finanzas
        addInstalacion.accept(srvAppSiafOpt, siafOpt);
        addInstalacion.accept(srvAppSiafOpt, ubuntuServerOpt);

        // SRV-APP-RRHH - Sistema de recursos humanos
        addInstalacion.accept(srvAppRrhhOpt, rrhhOpt);
        addInstalacion.accept(srvAppRrhhOpt, ubuntuServerOpt);

        // SRV-APP-TRAMITE - Trámite documentario
        addInstalacion.accept(srvAppTramiteOpt, tramiteOpt);
        addInstalacion.accept(srvAppTramiteOpt, mesaPartesOpt);
        addInstalacion.accept(srvAppTramiteOpt, ubuntuServerOpt);

        // SRV-APP-BIBLIOTECA - Sistema de biblioteca
        addInstalacion.accept(srvAppBibliotecaOpt, bibliotecaOpt);
        addInstalacion.accept(srvAppBibliotecaOpt, logisticaOpt);
        addInstalacion.accept(srvAppBibliotecaOpt, ubuntuServerOpt);

        // SRV-DC-PRIMARY - Controlador de dominio principal
        addInstalacion.accept(srvDcPrimaryOpt, windowsServerOpt);

        // SRV-DC-SECONDARY - Controlador de dominio secundario
        addInstalacion.accept(srvDcSecondaryOpt, windowsServerOpt);

        servidorAplicativoRepository.saveAll(instalaciones);
    }
}
