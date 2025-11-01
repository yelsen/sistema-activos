package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.licencias.domain.TipoLicencia;
import pe.edu.unasam.activos.modules.licencias.repository.TipoLicenciaRepository;

import java.util.Arrays;

@Component
@Order(12)
@RequiredArgsConstructor
public class TipoLicenciasDataLoader extends AbstractDataLoader {

    private final TipoLicenciaRepository tipoLicenciaRepository;

    @Override
    protected String getLoaderName() {
        return "Tipos de Licencia";
    }

    @Override
    protected boolean shouldLoad() {
        return tipoLicenciaRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        TipoLicencia[] tipos = {
                TipoLicencia.builder()
                        .tipoLicencia("Perpetua")
                        .descripcionTipoLicencia("Licencia de uso indefinido, pago único sin renovación")
                        .requiereRenovacion(false)
                        .build(),
                TipoLicencia.builder()
                        .tipoLicencia("Anual")
                        .descripcionTipoLicencia("Licencia con renovación anual obligatoria")
                        .requiereRenovacion(true)
                        .build(),
                TipoLicencia.builder()
                        .tipoLicencia("Mensual")
                        .descripcionTipoLicencia("Suscripción mensual con renovación automática")
                        .requiereRenovacion(true)
                        .build(),
                TipoLicencia.builder()
                        .tipoLicencia("Trial")
                        .descripcionTipoLicencia("Licencia de prueba temporal con funcionalidad limitada")
                        .requiereRenovacion(false)
                        .build(),
                TipoLicencia.builder()
                        .tipoLicencia("Educativa")
                        .descripcionTipoLicencia("Licencia especial para instituciones educativas con descuento")
                        .requiereRenovacion(true)
                        .build(),
                TipoLicencia.builder()
                        .tipoLicencia("Enterprise")
                        .descripcionTipoLicencia("Licencia corporativa con soporte premium y usuarios ilimitados")
                        .requiereRenovacion(true)
                        .build(),
                TipoLicencia.builder()
                        .tipoLicencia("Open Source")
                        .descripcionTipoLicencia("Licencia gratuita de código abierto")
                        .requiereRenovacion(false)
                        .build(),
                TipoLicencia.builder()
                        .tipoLicencia("Por Usuario")
                        .descripcionTipoLicencia("Licencia calculada por cantidad de usuarios concurrentes")
                        .requiereRenovacion(true)
                        .build(),
                TipoLicencia.builder()
                        .tipoLicencia("Sitio")
                        .descripcionTipoLicencia("Licencia para uso en una ubicación física específica")
                        .requiereRenovacion(true)
                        .build(),
                TipoLicencia.builder()
                        .tipoLicencia("OEM")
                        .descripcionTipoLicencia("Licencia incluida con hardware, no transferible")
                        .requiereRenovacion(false)
                        .build()
        };
        tipoLicenciaRepository.saveAll(Arrays.asList(tipos));
    }
}
