package pe.edu.unasam.activos.initialization.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.common.enums.EstadoRol;
import pe.edu.unasam.activos.initialization.AbstractDataLoader;
import pe.edu.unasam.activos.modules.sistema.domain.Rol; 
import pe.edu.unasam.activos.modules.sistema.repository.RolRepository;

@Component
@Order(4)
@RequiredArgsConstructor
public class RolDataLoader extends AbstractDataLoader {

    private final RolRepository rolRepository;
    
    @Override
    protected String getLoaderName() {
        return "Roles del Sistema";
    }
    
    @Override
    protected boolean shouldLoad() {
        return rolRepository.count() == 0;
    }
    
    @Override
    protected void loadData() {
        Rol[] roles = {
            Rol.builder()
                .nombreRol("ADMIN_GENERAL")
                .nivelAcceso(1)
                .colorRol("#DC2626") // Rojo
                .estadoRol(EstadoRol.ACTIVO)
                .build(),
            
            Rol.builder()
                .nombreRol("EDITOR")
                .nivelAcceso(2)
                .colorRol("#111111") // Negro
                .estadoRol(EstadoRol.ACTIVO)
                .build(),
            
            Rol.builder()
                .nombreRol("USUARIO_CONSULTA")
                .nivelAcceso(3)
                .colorRol("#059669") // Verde
                .estadoRol(EstadoRol.ACTIVO)
                .build()
        };
        
       for (Rol rol : roles) {
            rolRepository.save(rol);
        }
    }
}
