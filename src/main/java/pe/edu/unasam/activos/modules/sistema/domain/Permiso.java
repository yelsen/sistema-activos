package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permisos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Permiso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpermiso")
    private Integer idPermiso;
    
    @Column(name = "codigo_permiso", length = 50, nullable = false, unique = true)
    private String codigoPermiso;
    
    @Column(name = "nombre_permiso", length = 45, nullable = false)
    private String nombrePermiso;
    
    @Column(name = "descripcion_permiso", columnDefinition = "TEXT")
    private String descripcionPermiso;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idmodulo_sistemas", referencedColumnName = "idmodulo_sistemas")
    private ModuloSistema moduloSistema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idaccion", referencedColumnName = "idaccion")
    private Accion accion;
}
