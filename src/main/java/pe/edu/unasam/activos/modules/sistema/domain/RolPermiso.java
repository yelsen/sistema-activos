package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol_permisos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@IdClass(RolPermisoId.class)
public class RolPermiso {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idrol", referencedColumnName = "idrol")
    private Rol rol;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idpermiso", referencedColumnName = "idpermiso")
    private Permiso permiso;

    @Column(name = "permitido", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean permitido;
}
