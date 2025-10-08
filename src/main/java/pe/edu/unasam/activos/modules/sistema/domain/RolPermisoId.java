package pe.edu.unasam.activos.modules.sistema.domain;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RolPermisoId implements Serializable {
    private Integer rol;
    private Integer permiso;
}
