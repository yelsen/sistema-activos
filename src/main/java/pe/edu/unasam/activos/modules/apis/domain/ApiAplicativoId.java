package pe.edu.unasam.activos.modules.apis.domain;
import lombok.*;
import pe.edu.unasam.activos.modules.aplicativos.domain.Aplicativo;
import java.io.Serializable;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class ApiAplicativoId implements Serializable {

    private Aplicativo aplicativo;
    private Api api;
}
