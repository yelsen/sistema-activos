package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.common.enums.TipoUnidad;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.equipos.domain.Unidad;
import pe.edu.unasam.activos.modules.equipos.repository.UnidadRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(24)
@RequiredArgsConstructor
public class UnidadesDataLoader extends AbstractDataLoader {

    private final UnidadRepository unidadRepository;

    @Override
    protected String getLoaderName() {
        return "Unidades de Medida";
    }

    @Override
    protected boolean shouldLoad() {
        return unidadRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<Unidad> unidades = new ArrayList<>();

        // ==================== ALMACENAMIENTO ====================
        unidades.add(crearUnidad("Byte", "B", TipoUnidad.ALMACENAMIENTO));
        unidades.add(crearUnidad("Kilobyte", "KB", TipoUnidad.ALMACENAMIENTO));
        unidades.add(crearUnidad("Megabyte", "MB", TipoUnidad.ALMACENAMIENTO));
        unidades.add(crearUnidad("Gigabyte", "GB", TipoUnidad.ALMACENAMIENTO));
        unidades.add(crearUnidad("Terabyte", "TB", TipoUnidad.ALMACENAMIENTO));
        unidades.add(crearUnidad("Petabyte", "PB", TipoUnidad.ALMACENAMIENTO));

        // ==================== FRECUENCIA ====================
        unidades.add(crearUnidad("Hertz", "Hz", TipoUnidad.FRECUENCIA));
        unidades.add(crearUnidad("Kilohertz", "KHz", TipoUnidad.FRECUENCIA));
        unidades.add(crearUnidad("Megahertz", "MHz", TipoUnidad.FRECUENCIA));
        unidades.add(crearUnidad("Gigahertz", "GHz", TipoUnidad.FRECUENCIA));

        // ==================== MEMORIA ====================
        unidades.add(crearUnidad("Megabyte RAM", "MB", TipoUnidad.MEMORIA));
        unidades.add(crearUnidad("Gigabyte RAM", "GB", TipoUnidad.MEMORIA));
        unidades.add(crearUnidad("Terabyte RAM", "TB", TipoUnidad.MEMORIA));

        // ==================== ENERGÍA ====================
        unidades.add(crearUnidad("Watt", "W", TipoUnidad.ENERGIA));
        unidades.add(crearUnidad("Kilowatt", "KW", TipoUnidad.ENERGIA));
        unidades.add(crearUnidad("Watt-hora", "Wh", TipoUnidad.ENERGIA));
        unidades.add(crearUnidad("Kilowatt-hora", "KWh", TipoUnidad.ENERGIA));
        unidades.add(crearUnidad("Voltio", "V", TipoUnidad.ENERGIA));
        unidades.add(crearUnidad("Amperio", "A", TipoUnidad.ENERGIA));
        unidades.add(crearUnidad("Miliamperio-hora", "mAh", TipoUnidad.ENERGIA));

        // ==================== VELOCIDAD ====================
        unidades.add(crearUnidad("Bits por segundo", "bps", TipoUnidad.VELOCIDAD));
        unidades.add(crearUnidad("Kilobits por segundo", "Kbps", TipoUnidad.VELOCIDAD));
        unidades.add(crearUnidad("Megabits por segundo", "Mbps", TipoUnidad.VELOCIDAD));
        unidades.add(crearUnidad("Gigabits por segundo", "Gbps", TipoUnidad.VELOCIDAD));
        unidades.add(crearUnidad("Páginas por minuto", "ppm", TipoUnidad.VELOCIDAD));
        unidades.add(crearUnidad("Revoluciones por minuto", "RPM", TipoUnidad.VELOCIDAD));

        // ==================== DIMENSIONES / LONGITUD ====================
        unidades.add(crearUnidad("Milímetro", "mm", TipoUnidad.LONGITUD));
        unidades.add(crearUnidad("Centímetro", "cm", TipoUnidad.LONGITUD));
        unidades.add(crearUnidad("Metro", "m", TipoUnidad.LONGITUD));
        unidades.add(crearUnidad("Pulgada", "in", TipoUnidad.LONGITUD));
        unidades.add(crearUnidad("Pie", "ft", TipoUnidad.LONGITUD));

        // ==================== PESO ====================
        unidades.add(crearUnidad("Gramo", "g", TipoUnidad.PESO));
        unidades.add(crearUnidad("Kilogramo", "kg", TipoUnidad.PESO));
        unidades.add(crearUnidad("Libra", "lb", TipoUnidad.PESO));
        unidades.add(crearUnidad("Onza", "oz", TipoUnidad.PESO));

        // ==================== RESOLUCIÓN ====================
        unidades.add(crearUnidad("Puntos por pulgada", "dpi", TipoUnidad.RESOLUCION));
        unidades.add(crearUnidad("Píxeles por pulgada", "ppi", TipoUnidad.RESOLUCION));
        unidades.add(crearUnidad("Píxel", "px", TipoUnidad.RESOLUCION));

        // ==================== TEMPERATURA ====================
        unidades.add(crearUnidad("Grados Celsius", "°C", TipoUnidad.TEMPERATURA));
        unidades.add(crearUnidad("Grados Fahrenheit", "°F", TipoUnidad.TEMPERATURA));
        unidades.add(crearUnidad("Kelvin", "K", TipoUnidad.TEMPERATURA));

        // ==================== TIEMPO ====================
        unidades.add(crearUnidad("Segundo", "s", TipoUnidad.TIEMPO));
        unidades.add(crearUnidad("Minuto", "min", TipoUnidad.TIEMPO));
        unidades.add(crearUnidad("Hora", "h", TipoUnidad.TIEMPO));
        unidades.add(crearUnidad("Día", "d", TipoUnidad.TIEMPO));
        unidades.add(crearUnidad("Mes", "mes", TipoUnidad.TIEMPO));
        unidades.add(crearUnidad("Año", "año", TipoUnidad.TIEMPO));

        // ==================== PROCESAMIENTO ====================
        unidades.add(crearUnidad("Núcleos", "cores", TipoUnidad.PROCESAMIENTO));
        unidades.add(crearUnidad("Hilos", "threads", TipoUnidad.PROCESAMIENTO));
        unidades.add(crearUnidad("FLOPS", "FLOPS", TipoUnidad.PROCESAMIENTO));
        unidades.add(crearUnidad("MIPS", "MIPS", TipoUnidad.PROCESAMIENTO));

        // ==================== RED / RACK ====================
        unidades.add(crearUnidad("Unidad de rack", "U", TipoUnidad.RACK));
        unidades.add(crearUnidad("Puerto", "port", TipoUnidad.RED));
        unidades.add(crearUnidad("Canal", "channel", TipoUnidad.RED));

        // ==================== PORCENTAJE / CANTIDAD ====================
        unidades.add(crearUnidad("Porcentaje", "%", TipoUnidad.PORCENTAJE));
        unidades.add(crearUnidad("Unidad", "unidad", TipoUnidad.CANTIDAD));
        unidades.add(crearUnidad("Pieza", "pza", TipoUnidad.CANTIDAD));

        // ==================== SIN UNIDAD ====================
        unidades.add(crearUnidad("Sin unidad", "-", TipoUnidad.OTRO));
        unidades.add(crearUnidad("No aplica", "N/A", TipoUnidad.OTRO));

        unidadRepository.saveAll(unidades);
    }

    private Unidad crearUnidad(String nombre, String simbolo, TipoUnidad tipo) {
        return Unidad.builder()
                .nombreUnidad(nombre)
                .simboloUnidad(simbolo)
                .tipoUnidad(tipo)
                .build();
    }
}
