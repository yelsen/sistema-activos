 package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pe.edu.unasam.activos.common.enums.MetodoAutenticacion;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.apis.domain.Api;
import pe.edu.unasam.activos.modules.apis.repository.ApiRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(15)
@RequiredArgsConstructor
public class ApisDataLoader extends AbstractDataLoader {

    private final ApiRepository apiRepository;

    @Override
    protected String getLoaderName() {
        return "APIs";
    }

    @Override
    protected boolean shouldLoad() {
        return apiRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<Api> apis = new ArrayList<>();

        // APIs de Autenticación
        apis.add(Api.builder()
                .nombreApi("API Autenticación UNASAM")
                .descripcionApi("Servicio de autenticación centralizada OAuth2 para todos los sistemas UNASAM")
                .urlApi("https://auth.unasam.edu.pe/api/v1")
                .versionApi("1.5.0")
                .metodoAutenticacion(MetodoAutenticacion.OAUTH2)
                .build());

        apis.add(Api.builder()
                .nombreApi("API JWT Token Service")
                .descripcionApi("Generación y validación de tokens JWT para sesiones de usuario")
                .urlApi("https://auth.unasam.edu.pe/api/jwt")
                .versionApi("2.1.3")
                .metodoAutenticacion(MetodoAutenticacion.JWT)
                .build());

        // APIs de RENIEC y SUNAT
        apis.add(Api.builder()
                .nombreApi("API RENIEC Consulta DNI")
                .descripcionApi("Consulta de datos personales desde RENIEC para validación de identidad")
                .urlApi("https://api.reniec.gob.pe/v2/personas")
                .versionApi("2.0")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        apis.add(Api.builder()
                .nombreApi("API SUNAT RUC")
                .descripcionApi("Consulta de información de contribuyentes por RUC para proveedores")
                .urlApi("https://api.sunat.gob.pe/v1/contribuyente")
                .versionApi("1.8")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        // APIs Académicas
        apis.add(Api.builder()
                .nombreApi("API SIGA REST")
                .descripcionApi("API REST del Sistema Académico para matrícula, notas y registro estudiantil")
                .urlApi("https://siga.unasam.edu.pe/api/v2")
                .versionApi("2.3.0")
                .metodoAutenticacion(MetodoAutenticacion.BEARER_TOKEN)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Aula Virtual Moodle")
                .descripcionApi("Web services de Moodle para integración con sistema académico")
                .urlApi("https://aulavirtual.unasam.edu.pe/webservice/rest")
                .versionApi("4.1")
                .metodoAutenticacion(MetodoAutenticacion.BEARER_TOKEN)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Biblioteca Digital")
                .descripcionApi("Consulta de catálogo bibliográfico y gestión de préstamos")
                .urlApi("https://biblioteca.unasam.edu.pe/api/v1")
                .versionApi("1.4.2")
                .metodoAutenticacion(MetodoAutenticacion.OAUTH2)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Admisión")
                .descripcionApi("Gestión de inscripciones, pagos y resultados de procesos de admisión")
                .urlApi("https://admision.unasam.edu.pe/api")
                .versionApi("1.9.0")
                .metodoAutenticacion(MetodoAutenticacion.JWT)
                .build());

        // APIs Administrativas
        apis.add(Api.builder()
                .nombreApi("API SIAF Finanzas")
                .descripcionApi("Integración con sistema financiero para consultas presupuestales")
                .urlApi("https://siaf.unasam.edu.pe/api/v1")
                .versionApi("3.2.1")
                .metodoAutenticacion(MetodoAutenticacion.BEARER_TOKEN)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Recursos Humanos")
                .descripcionApi("Gestión de personal, asistencia y planillas")
                .urlApi("https://rrhh.unasam.edu.pe/api")
                .versionApi("2.5.0")
                .metodoAutenticacion(MetodoAutenticacion.OAUTH2)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Trámite Documentario")
                .descripcionApi("Seguimiento y gestión de expedientes digitales")
                .urlApi("https://tramite.unasam.edu.pe/api/v1")
                .versionApi("1.7.3")
                .metodoAutenticacion(MetodoAutenticacion.BEARER_TOKEN)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Logística")
                .descripcionApi("Control de almacén, órdenes de compra y distribución de bienes")
                .urlApi("https://logistica.unasam.edu.pe/api")
                .versionApi("2.8.1")
                .metodoAutenticacion(MetodoAutenticacion.BEARER_TOKEN)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Mesa de Partes Virtual")
                .descripcionApi("Registro y consulta de documentos entrantes y salientes")
                .urlApi("https://mesapartes.unasam.edu.pe/api/v2")
                .versionApi("2.0.5")
                .metodoAutenticacion(MetodoAutenticacion.OAUTH2)
                .build());

        // APIs de Pagos
        apis.add(Api.builder()
                .nombreApi("API Niubiz Pasarela")
                .descripcionApi("Procesamiento de pagos con tarjeta para servicios universitarios")
                .urlApi("https://apiprod.vnforapps.com/api.ecommerce")
                .versionApi("1.3")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        apis.add(Api.builder()
                .nombreApi("API PagoEfectivo")
                .descripcionApi("Generación de CIP para pagos en bancos y agentes")
                .urlApi("https://api.pagoefectivo.pe/v1")
                .versionApi("1.0")
                .metodoAutenticacion(MetodoAutenticacion.BASIC_AUTH)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Yape Empresas")
                .descripcionApi("Cobros mediante código QR de Yape para pagos universitarios")
                .urlApi("https://api.yape.com.pe/merchants/v1")
                .versionApi("1.2")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        // APIs de Comunicación
        apis.add(Api.builder()
                .nombreApi("API SendGrid Email")
                .descripcionApi("Envío masivo de correos electrónicos transaccionales y notificaciones")
                .urlApi("https://api.sendgrid.com/v3")
                .versionApi("3.0")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Twilio SMS")
                .descripcionApi("Envío de mensajes SMS para notificaciones urgentes")
                .urlApi("https://api.twilio.com/2010-04-01")
                .versionApi("2010-04-01")
                .metodoAutenticacion(MetodoAutenticacion.BASIC_AUTH)
                .build());

        apis.add(Api.builder()
                .nombreApi("API WhatsApp Business")
                .descripcionApi("Notificaciones automáticas vía WhatsApp a estudiantes")
                .urlApi("https://graph.facebook.com/v18.0")
                .versionApi("18.0")
                .metodoAutenticacion(MetodoAutenticacion.BEARER_TOKEN)
                .build());

        // APIs de Almacenamiento
        apis.add(Api.builder()
                .nombreApi("API Google Drive")
                .descripcionApi("Almacenamiento en la nube de documentos institucionales")
                .urlApi("https://www.googleapis.com/drive/v3")
                .versionApi("3.0")
                .metodoAutenticacion(MetodoAutenticacion.OAUTH2)
                .build());

        apis.add(Api.builder()
                .nombreApi("API AWS S3")
                .descripcionApi("Almacenamiento de archivos multimedia y backups")
                .urlApi("https://s3.amazonaws.com")
                .versionApi("2006-03-01")
                .metodoAutenticacion(MetodoAutenticacion.AWS_SIGNATURE)
                .build());

        // APIs de Mapas y Geolocalización
        apis.add(Api.builder()
                .nombreApi("API Google Maps")
                .descripcionApi("Mapas y geolocalización para campus universitarios")
                .urlApi("https://maps.googleapis.com/maps/api")
                .versionApi("3.0")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        // APIs de Reportes
        apis.add(Api.builder()
                .nombreApi("API JasperReports Server")
                .descripcionApi("Generación de reportes PDF y Excel desde sistemas institucionales")
                .urlApi("https://reportes.unasam.edu.pe/jasperserver/rest_v2")
                .versionApi("8.2")
                .metodoAutenticacion(MetodoAutenticacion.BASIC_AUTH)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Power BI Embedded")
                .descripcionApi("Integración de dashboards analíticos en aplicaciones web")
                .urlApi("https://api.powerbi.com/v1.0")
                .versionApi("1.0")
                .metodoAutenticacion(MetodoAutenticacion.OAUTH2)
                .build());

        // APIs de Seguridad
        apis.add(Api.builder()
                .nombreApi("API reCAPTCHA v3")
                .descripcionApi("Protección contra bots en formularios de inscripción")
                .urlApi("https://www.google.com/recaptcha/api/siteverify")
                .versionApi("3.0")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        // APIs de Monitoreo
        apis.add(Api.builder()
                .nombreApi("API Prometheus Metrics")
                .descripcionApi("Recolección de métricas de rendimiento de servidores")
                .urlApi("https://monitor.unasam.edu.pe/api/v1")
                .versionApi("1.0")
                .metodoAutenticacion(MetodoAutenticacion.BEARER_TOKEN)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Grafana")
                .descripcionApi("Visualización de métricas y alertas de infraestructura")
                .urlApi("https://grafana.unasam.edu.pe/api")
                .versionApi("10.0")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        // APIs Externas Académicas
        apis.add(Api.builder()
                .nombreApi("API SUNEDU Licenciamiento")
                .descripcionApi("Consulta de información de licenciamiento institucional")
                .urlApi("https://api.sunedu.gob.pe/api/v1")
                .versionApi("1.0")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        apis.add(Api.builder()
                .nombreApi("API Scopus Citation")
                .descripcionApi("Consulta de índices de citación para investigadores")
                .urlApi("https://api.elsevier.com/content/search/scopus")
                .versionApi("2.0")
                .metodoAutenticacion(MetodoAutenticacion.API_KEY)
                .build());

        apiRepository.saveAll(apis);
    }
}
