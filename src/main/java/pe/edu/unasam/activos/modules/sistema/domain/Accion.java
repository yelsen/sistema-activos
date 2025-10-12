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
@Table(name = "acciones")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLDelete(sql = "UPDATE acciones SET deleted_at = NOW() WHERE idaccion = ?")
@SQLRestriction("deleted_at IS NULL")
public class Accion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idaccion")
    private Integer idAccion;

    @Column(name = "nombre_accion", nullable = false, length = 50)
    private String nombreAccion;

    @Column(name = "codigo_accion", nullable = false, unique = true, length = 20)
    private String codigoAccion;

    @Column(name = "descripcion_accion")
    private String descripcionAccion;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;
}