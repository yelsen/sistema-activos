package pe.edu.unasam.activos.modules.equipos.domain;

import java.io.Serializable;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class EquipoEspecificacionId implements Serializable {
    private Equipo equipo;
    private Componente componente;
}
