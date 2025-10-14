package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;
import pe.edu.unasam.activos.modules.sistema.repository.ConfiguracionSistemaRepository;

@Component
@Order(9)
@RequiredArgsConstructor
public class ConfiguracionSistemaDataLoader extends AbstractDataLoader {

    private final ConfiguracionSistemaRepository configuracionSistemaRepository;

    @Override
    protected String getLoaderName() {
        return "Configuración del Sistema";
    }

    @Override
    protected boolean shouldLoad() {
        return configuracionSistemaRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        ConfiguracionSistema[] configuraciones = {
                ConfiguracionSistema.builder()
                        .claveConfig("seguridad.intentos_fallidos")
                        .valorConfig("5")
                        .descripcionConfig("Número máximo de intentos fallidos antes de bloquear usuario")
                        .categoriaConfig("SEGURIDAD")
                        .usuarioModificacion("SISTEMA")
                        .build(),

                ConfiguracionSistema.builder()
                        .claveConfig("seguridad.tiempo_bloqueo_minutos")
                        .valorConfig("15")
                        .descripcionConfig("Tiempo de bloqueo en minutos después de intentos fallidos")
                        .categoriaConfig("SEGURIDAD")
                        .usuarioModificacion("SISTEMA")
                        .build(),

                ConfiguracionSistema.builder()
                        .claveConfig("seguridad.longitud_minima_password")
                        .valorConfig("8")
                        .descripcionConfig("Longitud mínima requerida para contraseñas")
                        .categoriaConfig("SEGURIDAD")
                        .usuarioModificacion("SISTEMA")
                        .build(),

                ConfiguracionSistema.builder()
                        .claveConfig("sesion.tiempo_expiracion_horas")
                        .valorConfig("24")
                        .descripcionConfig("Tiempo de expiración de sesión en horas")
                        .categoriaConfig("SESION")
                        .usuarioModificacion("SISTEMA")
                        .build(),

                ConfiguracionSistema.builder()
                        .claveConfig("sistema.nombre")
                        .valorConfig("Sistema de Gestión de Activos Fijos - UNASAM")
                        .descripcionConfig("Nombre del sistema")
                        .categoriaConfig("SISTEMA")
                        .usuarioModificacion("SISTEMA")
                        .build(),

                ConfiguracionSistema.builder()
                        .claveConfig("sistema.version")
                        .valorConfig("1.0.0")
                        .descripcionConfig("Versión del sistema")
                        .categoriaConfig("SISTEMA")
                        .usuarioModificacion("SISTEMA")
                        .build(),

                ConfiguracionSistema.builder()
                        .claveConfig("sistema.mantenimiento")
                        .valorConfig("false")
                        .descripcionConfig("Modo mantenimiento del sistema")
                        .categoriaConfig("SISTEMA")
                        .usuarioModificacion("SISTEMA")
                        .build(),

                ConfiguracionSistema.builder()
                        .claveConfig("email.smtp.host")
                        .valorConfig("smtp.unasam.edu.pe")
                        .descripcionConfig("Servidor SMTP para envío de correos")
                        .categoriaConfig("EMAIL")
                        .usuarioModificacion("SISTEMA")
                        .build()
        };

        configuracionSistemaRepository.saveAll(java.util.Arrays.asList(configuraciones));
    }
}
