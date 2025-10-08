package pe.edu.unasam.activos.modules.servidores.domain;

import java.io.Serializable;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class ServidorRecursoId implements Serializable {
    private Integer servidor;
    private Integer recurso;
}
