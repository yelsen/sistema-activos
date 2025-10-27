package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.personas.domain.Cargo;
import pe.edu.unasam.activos.modules.personas.repository.CargoRepository;

import java.util.Arrays;

@Component
@Order(11)
@RequiredArgsConstructor
public class CargosDataLoader extends AbstractDataLoader {

    private final CargoRepository cargoRepository;

    @Override
    protected String getLoaderName() {
        return "Cargos";
    }

    @Override
    protected boolean shouldLoad() {
        return cargoRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        Cargo[] cargos = {
                Cargo.builder().nombreCargo("Rector").build(),
                Cargo.builder().nombreCargo("Vicerrector Académico").build(),
                Cargo.builder().nombreCargo("Vicerrector de Investigación").build(),
                Cargo.builder().nombreCargo("Decano").build(),
                Cargo.builder().nombreCargo("Director de Departamento").build(),
                Cargo.builder().nombreCargo("Jefe de Oficina").build(),
                Cargo.builder().nombreCargo("Secretario General").build(),
                Cargo.builder().nombreCargo("Asistente Administrativo").build(),
                Cargo.builder().nombreCargo("Técnico de Soporte").build()
        };
        cargoRepository.saveAll(Arrays.asList(cargos));
    }
}
