package pe.edu.unasam.activos.modules.aplicativos.domain;

import java.io.Serializable;
import lombok.*;
import pe.edu.unasam.activos.modules.equipos.domain.Equipo;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class InstalacionAplicativoId implements Serializable {
    private Equipo equipo;
    private Aplicativo aplicativo;
}
