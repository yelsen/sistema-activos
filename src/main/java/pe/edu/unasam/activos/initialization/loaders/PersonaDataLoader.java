package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.common.enums.Genero;
import pe.edu.unasam.activos.common.enums.EstadoPersona;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.personas.repository.PersonaRepository;

@Component
@Order(7)
@RequiredArgsConstructor
public class PersonaDataLoader extends AbstractDataLoader {

    private final PersonaRepository personaRepository;
    
    @Override
    protected String getLoaderName() {
        return "Personas";
    }
    
    @Override
    protected boolean shouldLoad() {
        return personaRepository.count() == 0;
    }
    
    @Override
    protected void loadData() {
        
        Persona[] personas = {
            Persona.builder()
                .dni("12345678")
                .apellidos("Guevarra del Campo")
                .nombres("Klisha")
                .email("admin@unasam.edu.pe")
                .telefono("999999999")
                .direccion("UNASAM - Shancayan")
                .genero(Genero.FEMENINO)
                .estadoPersona(EstadoPersona.ACTIVO)
                .build(),
            
            Persona.builder()
                .dni("87654321")
                .apellidos("García Pérez")
                .nombres("Juan Carlos")
                .email("jgarcia@unasam.edu.pe")
                .telefono("987654321")
                .direccion("Av. Centenario 200")
                .genero(Genero.MASCULINO)
                .estadoPersona(EstadoPersona.ACTIVO)
                .build(),
            
            Persona.builder()
                .dni("11223344")
                .apellidos("Rodríguez Silva")
                .nombres("María Elena")
                .email("mrodriguez@unasam.edu.pe")
                .telefono("976543210")
                .direccion("Jr. San Martín 456")
                .genero(Genero.FEMENINO)
                .estadoPersona(EstadoPersona.ACTIVO)
                .build()
        };
        
        personaRepository.saveAll(java.util.Arrays.asList(personas));
    }
}
