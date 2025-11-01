package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.equipos.domain.CategoriaComponente;
import pe.edu.unasam.activos.modules.equipos.repository.CategoriaComponenteRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(23)
@RequiredArgsConstructor
public class CategoriasComponentesDataLoader extends AbstractDataLoader {

    private final CategoriaComponenteRepository categoriaComponenteRepository;

    @Override
    protected String getLoaderName() {
        return "Categorías de Componentes";
    }

    @Override
    protected boolean shouldLoad() {
        return categoriaComponenteRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<CategoriaComponente> categorias = new ArrayList<>();

        categorias.add(CategoriaComponente.builder().categoriaComponente("Sistema Operativo").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Procesador").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Memoria RAM").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Almacenamiento").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Tarjeta Madre").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Tarjeta de Video").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Pantalla").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Conectividad").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Puertos").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Batería").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Impresión").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Red").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Energía").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Dimensiones").build());
        categorias.add(CategoriaComponente.builder().categoriaComponente("Peso").build());

        categoriaComponenteRepository.saveAll(categorias);
    }
}
