package pe.edu.unasam.activos.modules.aplicativos.domain;
import pe.edu.unasam.activos.modules.servidores.domain.Servidor;
import java.io.Serializable;
import lombok.*;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class ServidorAplicativoId implements Serializable {
    private Servidor servidor;
    private Aplicativo aplicativo;
}
