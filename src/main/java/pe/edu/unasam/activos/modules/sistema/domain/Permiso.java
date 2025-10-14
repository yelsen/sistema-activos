package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "permisos")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE permisos SET deleted_at = NOW() WHERE idpermiso = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;
}
