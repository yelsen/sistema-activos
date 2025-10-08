package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.personas.domain.TipoDocumento;
import pe.edu.unasam.activos.modules.personas.repository.TipoDocumentoRepository;

@Component
@Order(1)
@RequiredArgsConstructor
public class TipoDocumentoDataLoader extends AbstractDataLoader {

    private final TipoDocumentoRepository tipoDocumentoRepository;
    
    @Override
    protected String getLoaderName() {
        return "Tipos de Documento";
    }
    
    @Override
    protected boolean shouldLoad() {
        return tipoDocumentoRepository.count() == 0;
    }
    
    @Override
    protected void loadData() {
        TipoDocumento[] tiposDocumento = {
            TipoDocumento.builder()
                .tipoDocumento("DNI")
                .longitudMinima("8")
                .longitudMaxima("8")
                .patronValidacion("^[0-9]{8}$")
                .build(),
            
            TipoDocumento.builder()
                .tipoDocumento("Carnet de Extranjería")
                .longitudMinima("9")
                .longitudMaxima("12")
                .patronValidacion("^[A-Z0-9]{9,12}$")
                .build(),
            
            TipoDocumento.builder()
                .tipoDocumento("Pasaporte")
                .longitudMinima("7")
                .longitudMaxima("12")
                .patronValidacion("^[A-Z0-9]{7,12}$")
                .build(),
            
            TipoDocumento.builder()
                .tipoDocumento("RUC")
                .longitudMinima("11")
                .longitudMaxima("11")
                .patronValidacion("^[0-9]{11}$")
                .build()
        };
        
        for (TipoDocumento tipo : tiposDocumento) {
            tipoDocumentoRepository.save(tipo);
            //log.info("Tipo documento creado: {}", tipo.getTipoDocumento());
        }
    }
}
