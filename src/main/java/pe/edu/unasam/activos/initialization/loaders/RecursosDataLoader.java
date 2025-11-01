package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.common.enums.TipoRecurso;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.servidores.domain.Recurso;
import pe.edu.unasam.activos.modules.servidores.repository.RecursoRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(25)
@RequiredArgsConstructor
public class RecursosDataLoader extends AbstractDataLoader {

    private final RecursoRepository recursoRepository;

    @Override
    protected String getLoaderName() {
        return "Recursos";
    }

    @Override
    protected boolean shouldLoad() {
        return recursoRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<Recurso> recursos = new ArrayList<>();

        // CPU
        recursos.add(Recurso.builder()
                .nombreRecurso("CPU Cores")
                .descripcionRecurso("Número de núcleos de CPU")
                .tipoRecurso(TipoRecurso.CPU)
                .build());

        recursos.add(Recurso.builder()
                .nombreRecurso("CPU Frequency")
                .descripcionRecurso("Frecuencia base del CPU")
                .tipoRecurso(TipoRecurso.CPU)
                .build());

        recursos.add(Recurso.builder()
                .nombreRecurso("CPU Model")
                .descripcionRecurso("Modelo del procesador")
                .tipoRecurso(TipoRecurso.CPU)
                .build());

        // RAM
        recursos.add(Recurso.builder()
                .nombreRecurso("RAM Total")
                .descripcionRecurso("Memoria RAM total instalada")
                .tipoRecurso(TipoRecurso.MEMORIA_RAM)
                .build());

        recursos.add(Recurso.builder()
                .nombreRecurso("RAM Type")
                .descripcionRecurso("Tipo de memoria RAM instalada")
                .tipoRecurso(TipoRecurso.MEMORIA_RAM)
                .build());

        // Almacenamiento
        recursos.add(Recurso.builder()
                .nombreRecurso("Storage Total")
                .descripcionRecurso("Capacidad total de almacenamiento")
                .tipoRecurso(TipoRecurso.DISCO_DURO)
                .build());

        recursos.add(Recurso.builder()
                .nombreRecurso("Storage Type")
                .descripcionRecurso("Tipo de almacenamiento (SSD/HDD/NVMe)")
                .tipoRecurso(TipoRecurso.DISCO_DURO)
                .build());

        // Red
        recursos.add(Recurso.builder()
                .nombreRecurso("Network Speed")
                .descripcionRecurso("Velocidad de red disponible")
                .tipoRecurso(TipoRecurso.ANCHO_BANDA)
                .build());

        // Sistema Operativo
        recursos.add(Recurso.builder()
                .nombreRecurso("OS Version")
                .descripcionRecurso("Versión del sistema operativo instalado")
                .tipoRecurso(null)
                .build());

        // Energía
        recursos.add(Recurso.builder()
                .nombreRecurso("Power Supply")
                .descripcionRecurso("Capacidad de la fuente de poder")
                .tipoRecurso(TipoRecurso.ENERGIA)
                .build());

        recursoRepository.saveAll(recursos);
    }
}