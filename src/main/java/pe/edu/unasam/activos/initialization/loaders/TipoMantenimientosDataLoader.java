package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.mantenimientos.domain.TipoMantenimiento;
import pe.edu.unasam.activos.modules.mantenimientos.repository.TipoMantenimientoRepository;

import java.util.Arrays;

@Component
@Order(20)
@RequiredArgsConstructor
public class TipoMantenimientosDataLoader extends AbstractDataLoader {

    private final TipoMantenimientoRepository tipoMantenimientoRepository;

    @Override
    protected String getLoaderName() {
        return "Tipos de Mantenimiento";
    }

    @Override
    protected boolean shouldLoad() {
        return tipoMantenimientoRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        TipoMantenimiento[] tipos = {
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Preventivo")
                        .build(),
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Correctivo")
                        .build(),
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Predictivo")
                        .build(),
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Actualización")
                        .build(),
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Limpieza")
                        .build(),
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Diagnóstico")
                        .build(),
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Instalación")
                        .build(),
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Desinstalación")
                        .build(),
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Reemplazo")
                        .build(),
                TipoMantenimiento.builder()
                        .tipoMantenimiento("Calibración")
                        .build()
        };
        tipoMantenimientoRepository.saveAll(Arrays.asList(tipos));
    }
}
