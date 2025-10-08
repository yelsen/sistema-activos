package pe.edu.unasam.activos.modules.licencias.domain;

import java.io.Serializable;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class LicenciaAsignacionId implements Serializable {
    private Integer licencia;
    private Integer oficina;
}