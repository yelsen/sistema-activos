package pe.edu.unasam.activos.modules.aplicativos.domain;

import java.io.Serializable;
import lombok.*;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class ServidorAplicativoId implements Serializable {
    private Integer servidor;
    private Integer aplicativo;
}
