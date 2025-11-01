package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pe.edu.unasam.activos.common.enums.EstadoIntegracion;
import pe.edu.unasam.activos.common.enums.UsoApi;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.apis.domain.Api;
import pe.edu.unasam.activos.modules.apis.domain.ApiAplicativo;
import pe.edu.unasam.activos.modules.apis.repository.ApiAplicativoRepository;
import pe.edu.unasam.activos.modules.apis.repository.ApiRepository;
import pe.edu.unasam.activos.modules.aplicativos.domain.Aplicativo;
import pe.edu.unasam.activos.modules.aplicativos.repository.AplicativoRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(28)
@RequiredArgsConstructor
public class ApiAplicativosDataLoader extends AbstractDataLoader {

    private final ApiAplicativoRepository apiAplicativoRepository;
    private final AplicativoRepository aplicativoRepository;
    private final ApiRepository apiRepository;

    @Override
    protected String getLoaderName() {
        return "Relaciones API-Aplicativo";
    }

    @Override
    protected boolean shouldLoad() {
        return apiAplicativoRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        // Buscar aplicativos
        Aplicativo sigaUnasam = aplicativoRepository.findByNombreAplicativo("SIGA UNASAM").orElseThrow();
        Aplicativo aulaVirtual = aplicativoRepository.findByNombreAplicativo("Aula Virtual UNASAM").orElseThrow();
        Aplicativo biblioteca = aplicativoRepository.findByNombreAplicativo("Sistema de Biblioteca").orElseThrow();
        Aplicativo admision = aplicativoRepository.findByNombreAplicativo("Portal de Admisión").orElseThrow();
        Aplicativo siaf = aplicativoRepository.findByNombreAplicativo("SIAF Universidad").orElseThrow();
        Aplicativo rrhh = aplicativoRepository.findByNombreAplicativo("Sistema de Recursos Humanos").orElseThrow();
        Aplicativo tramite = aplicativoRepository.findByNombreAplicativo("Sistema de Trámite Documentario").orElseThrow();
        Aplicativo logistica = aplicativoRepository.findByNombreAplicativo("Sistema de Logística").orElseThrow();
        Aplicativo mesaPartes = aplicativoRepository.findByNombreAplicativo("Sistema de Mesa de Partes").orElseThrow();

        // Buscar APIs
        Api authApi = apiRepository.findByNombreApi("API Autenticación UNASAM").orElseThrow();
        Api jwtApi = apiRepository.findByNombreApi("API JWT Token Service").orElseThrow();
        Api reniecApi = apiRepository.findByNombreApi("API RENIEC Consulta DNI").orElseThrow();
        Api sunatApi = apiRepository.findByNombreApi("API SUNAT RUC").orElseThrow();
        Api sigaRestApi = apiRepository.findByNombreApi("API SIGA REST").orElseThrow();
        Api moodleApi = apiRepository.findByNombreApi("API Aula Virtual Moodle").orElseThrow();
        Api bibliotecaApi = apiRepository.findByNombreApi("API Biblioteca Digital").orElseThrow();
        Api admisionApi = apiRepository.findByNombreApi("API Admisión").orElseThrow();
        Api siafApi = apiRepository.findByNombreApi("API SIAF Finanzas").orElseThrow();
        Api rrhhApi = apiRepository.findByNombreApi("API Recursos Humanos").orElseThrow();
        Api tramiteApi = apiRepository.findByNombreApi("API Trámite Documentario").orElseThrow();
        Api logisticaApi = apiRepository.findByNombreApi("API Logística").orElseThrow();
        Api mesaPartesApi = apiRepository.findByNombreApi("API Mesa de Partes Virtual").orElseThrow();
        Api niubizApi = apiRepository.findByNombreApi("API Niubiz Pasarela").orElseThrow();
        Api pagoEfectivoApi = apiRepository.findByNombreApi("API PagoEfectivo").orElseThrow();
        Api sendgridApi = apiRepository.findByNombreApi("API SendGrid Email").orElseThrow();
        Api twilioApi = apiRepository.findByNombreApi("API Twilio SMS").orElseThrow();
        Api whatsappApi = apiRepository.findByNombreApi("API WhatsApp Business").orElseThrow();
        Api googleDriveApi = apiRepository.findByNombreApi("API Google Drive").orElseThrow();
        Api jasperApi = apiRepository.findByNombreApi("API JasperReports Server").orElseThrow();
        Api recaptchaApi = apiRepository.findByNombreApi("API reCAPTCHA v3").orElseThrow();

        List<ApiAplicativo> relaciones = new ArrayList<>();

        // SIGA UNASAM usa múltiples APIs
        relaciones.add(ApiAplicativo.builder()
                .aplicativo(sigaUnasam)
                .api(authApi)
                .uso(UsoApi.AUTENTICACION)
                .versionIntegracion("1.5.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(sigaUnasam)
                .api(jwtApi)
                .uso(UsoApi.SESIONES)
                .versionIntegracion("2.1.3")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(sigaUnasam)
                .api(reniecApi)
                .uso(UsoApi.VALIDACION)
                .versionIntegracion("2.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(sigaUnasam)
                .api(sigaRestApi)
                .uso(UsoApi.PRINCIPAL)
                .versionIntegracion("2.3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(sigaUnasam)
                .api(sendgridApi)
                .uso(UsoApi.NOTIFICACIONES)
                .versionIntegracion("3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(sigaUnasam)
                .api(jasperApi)
                .uso(UsoApi.REPORTES)
                .versionIntegracion("8.2")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        // Aula Virtual usa
        relaciones.add(ApiAplicativo.builder()
                .aplicativo(aulaVirtual)
                .api(authApi)
                .uso(UsoApi.AUTENTICACION)
                .versionIntegracion("1.5.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(aulaVirtual)
                .api(moodleApi)
                .uso(UsoApi.PRINCIPAL)
                .versionIntegracion("4.1")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(aulaVirtual)
                .api(sigaRestApi)
                .uso(UsoApi.INTEGRACION)
                .versionIntegracion("2.3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(aulaVirtual)
                .api(googleDriveApi)
                .uso(UsoApi.ALMACENAMIENTO)
                .versionIntegracion("3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        // Sistema de Biblioteca
        relaciones.add(ApiAplicativo.builder()
                .aplicativo(biblioteca)
                .api(authApi)
                .uso(UsoApi.AUTENTICACION)
                .versionIntegracion("1.5.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(biblioteca)
                .api(bibliotecaApi)
                .uso(UsoApi.PRINCIPAL)
                .versionIntegracion("1.4.2")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(biblioteca)
                .api(sigaRestApi)
                .uso(UsoApi.VALIDACION)
                .versionIntegracion("2.3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(biblioteca)
                .api(sendgridApi)
                .uso(UsoApi.NOTIFICACIONES)
                .versionIntegracion("3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        // Portal de Admisión
        relaciones.add(ApiAplicativo.builder()
                .aplicativo(admision)
                .api(jwtApi)
                .uso(UsoApi.AUTENTICACION)
                .versionIntegracion("2.1.3")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(admision)
                .api(admisionApi)
                .uso(UsoApi.PRINCIPAL)
                .versionIntegracion("1.9.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(admision)
                .api(reniecApi)
                .uso(UsoApi.VALIDACION)
                .versionIntegracion("2.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(admision)
                .api(niubizApi)
                .uso(UsoApi.PAGOS)
                .versionIntegracion("1.3")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(admision)
                .api(pagoEfectivoApi)
                .uso(UsoApi.PAGOS)
                .versionIntegracion("1.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(admision)
                .api(recaptchaApi)
                .uso(UsoApi.SEGURIDAD)
                .versionIntegracion("3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(admision)
                .api(sendgridApi)
                .uso(UsoApi.NOTIFICACIONES)
                .versionIntegracion("3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(admision)
                .api(twilioApi)
                .uso(UsoApi.SMS)
                .versionIntegracion("2010-04-01")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        // SIAF Universidad
        relaciones.add(ApiAplicativo.builder()
                .aplicativo(siaf)
                .api(authApi)
                .uso(UsoApi.AUTENTICACION)
                .versionIntegracion("1.5.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(siaf)
                .api(siafApi)
                .uso(UsoApi.PRINCIPAL)
                .versionIntegracion("3.2.1")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(siaf)
                .api(sunatApi)
                .uso(UsoApi.VALIDACION)
                .versionIntegracion("1.8")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(siaf)
                .api(jasperApi)
                .uso(UsoApi.REPORTES)
                .versionIntegracion("8.2")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        // Recursos Humanos
        relaciones.add(ApiAplicativo.builder()
                .aplicativo(rrhh)
                .api(authApi)
                .uso(UsoApi.AUTENTICACION)
                .versionIntegracion("1.5.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(rrhh)
                .api(rrhhApi)
                .uso(UsoApi.PRINCIPAL)
                .versionIntegracion("2.5.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(rrhh)
                .api(reniecApi)
                .uso(UsoApi.VALIDACION)
                .versionIntegracion("2.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(rrhh)
                .api(sendgridApi)
                .uso(UsoApi.NOTIFICACIONES)
                .versionIntegracion("3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        // Trámite Documentario
        relaciones.add(ApiAplicativo.builder()
                .aplicativo(tramite)
                .api(authApi)
                .uso(UsoApi.AUTENTICACION)
                .versionIntegracion("1.5.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(tramite)
                .api(tramiteApi)
                .uso(UsoApi.PRINCIPAL)
                .versionIntegracion("1.7.3")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(tramite)
                .api(googleDriveApi)
                .uso(UsoApi.ALMACENAMIENTO)
                .versionIntegracion("3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(tramite)
                .api(whatsappApi)
                .uso(UsoApi.NOTIFICACIONES)
                .versionIntegracion("18.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        // Sistema de Logística
        relaciones.add(ApiAplicativo.builder()
                .aplicativo(logistica)
                .api(authApi)
                .uso(UsoApi.AUTENTICACION)
                .versionIntegracion("1.5.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(logistica)
                .api(logisticaApi)
                .uso(UsoApi.PRINCIPAL)
                .versionIntegracion("2.8.1")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(logistica)
                .api(sunatApi)
                .uso(UsoApi.VALIDACION)
                .versionIntegracion("1.8")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(logistica)
                .api(jasperApi)
                .uso(UsoApi.REPORTES)
                .versionIntegracion("8.2")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        // Mesa de Partes
        relaciones.add(ApiAplicativo.builder()
                .aplicativo(mesaPartes)
                .api(authApi)
                .uso(UsoApi.AUTENTICACION)
                .versionIntegracion("1.5.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(mesaPartes)
                .api(mesaPartesApi)
                .uso(UsoApi.PRINCIPAL)
                .versionIntegracion("2.0.5")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(mesaPartes)
                .api(tramiteApi)
                .uso(UsoApi.INTEGRACION)
                .versionIntegracion("1.7.3")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        relaciones.add(ApiAplicativo.builder()
                .aplicativo(mesaPartes)
                .api(sendgridApi)
                .uso(UsoApi.NOTIFICACIONES)
                .versionIntegracion("3.0")
                .estadoIntegracion(EstadoIntegracion.ACTIVO)
                .build());

        apiAplicativoRepository.saveAll(relaciones);
    }
}
