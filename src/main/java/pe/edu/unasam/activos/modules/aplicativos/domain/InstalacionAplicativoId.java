package pe.edu.unasam.activos.modules.aplicativos.domain;

import java.io.Serializable;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class InstalacionAplicativoId implements Serializable {
    private Integer equipo;
    private Integer aplicativo;
}
