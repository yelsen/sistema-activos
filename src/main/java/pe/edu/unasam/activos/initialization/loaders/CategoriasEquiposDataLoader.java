package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.equipos.domain.CategoriaEquipo;
import pe.edu.unasam.activos.modules.equipos.repository.CategoriaEquipoRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(22)
@RequiredArgsConstructor
public class CategoriasEquiposDataLoader extends AbstractDataLoader {

    private final CategoriaEquipoRepository categoriaEquipoRepository;

    @Override
    protected String getLoaderName() {
        return "Categorías de Equipos";
    }

    @Override
    protected boolean shouldLoad() {
        return categoriaEquipoRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<CategoriaEquipo> categorias = new ArrayList<>();

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Computadora de Escritorio")
                .descripcionCategoriaEquipo("Equipo de cómputo fijo para oficinas y laboratorios")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Laptop")
                .descripcionCategoriaEquipo("Equipo portátil para trabajo académico y administrativo")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Servidor")
                .descripcionCategoriaEquipo("Equipo para servicios de red, aplicaciones y bases de datos")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Impresora")
                .descripcionCategoriaEquipo("Dispositivo de impresión láser o inyección de tinta")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Escáner")
                .descripcionCategoriaEquipo("Dispositivo para digitalización de documentos")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Proyector")
                .descripcionCategoriaEquipo("Dispositivo para proyección en aulas y auditorios")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Switch de Red")
                .descripcionCategoriaEquipo("Equipo para conmutación y distribución de red local")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Router")
                .descripcionCategoriaEquipo("Equipo de enrutamiento y acceso a internet")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Punto de Acceso")
                .descripcionCategoriaEquipo("Dispositivo para red inalámbrica (WiFi)")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("UPS")
                .descripcionCategoriaEquipo("Sistema de alimentación ininterrumpida para protección de equipos")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Monitor")
                .descripcionCategoriaEquipo("Pantalla para estaciones de trabajo y equipos")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Tablet")
                .descripcionCategoriaEquipo("Dispositivo móvil para actividades académicas y administrativas")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Smartphone")
                .descripcionCategoriaEquipo("Teléfono inteligente asignado como activo tecnológico")
                .build());

        categorias.add(CategoriaEquipo.builder()
                .categoriaEquipo("Cámara IP")
                .descripcionCategoriaEquipo("Dispositivo de video vigilancia conectado a red")
                .build());

        categoriaEquipoRepository.saveAll(categorias);
    }
}