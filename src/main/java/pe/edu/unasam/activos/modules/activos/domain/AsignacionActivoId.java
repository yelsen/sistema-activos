package pe.edu.unasam.activos.modules.activos.domain;

import lombok.*;
import java.io.Serializable;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionActivoId implements Serializable {
    private Activo activo;
    private Oficina oficina;
}
