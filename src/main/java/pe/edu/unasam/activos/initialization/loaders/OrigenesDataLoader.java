package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Origen;
import pe.edu.unasam.activos.modules.ubicaciones.repository.OrigenRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(17)
@RequiredArgsConstructor
public class OrigenesDataLoader extends AbstractDataLoader {

    private final OrigenRepository origenRepository;

    @Override
    protected String getLoaderName() {
        return "Orígenes de Activos";
    }

    @Override
    protected boolean shouldLoad() {
        return origenRepository.count() == 0;
    }

    @Override
    protected void loadData() {
        List<Origen> origenes = new ArrayList<>();

        origenes.add(Origen.builder().nombreOrigen("Compra").build());
        origenes.add(Origen.builder().nombreOrigen("Donación").build());
        origenes.add(Origen.builder().nombreOrigen("Transferencia Interna").build());
        origenes.add(Origen.builder().nombreOrigen("Cesión en Uso").build());
        origenes.add(Origen.builder().nombreOrigen("Convenio Interinstitucional").build());
        origenes.add(Origen.builder().nombreOrigen("Adquisición por Proyecto").build());
        origenes.add(Origen.builder().nombreOrigen("Licitación/Adjudicación").build());

        origenRepository.saveAll(origenes);
    }
}