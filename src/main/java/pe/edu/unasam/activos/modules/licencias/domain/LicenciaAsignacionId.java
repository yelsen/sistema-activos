package pe.edu.unasam.activos.modules.licencias.domain;
import java.io.Serializable;
import lombok.*;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class LicenciaAsignacionId implements Serializable {
    private Licencia licencia;
    private Oficina oficina;
}