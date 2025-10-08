package pe.edu.unasam.activos.modules.apis.domain;
import lombok.*;
import java.io.Serializable;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class ApiAplicativoId implements Serializable {
    
    private Integer aplicativo;
    private Integer api;
}
