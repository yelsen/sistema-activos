package pe.edu.unasam.activos.modules.sistema.domain;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RolPermisoId implements Serializable {
    private Rol rol;
    private Permiso permiso;
}
