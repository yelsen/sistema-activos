package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "especificaciones")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE especificaciones SET deleted_at = NOW() WHERE idespecificacion = ?")
@SQLRestriction("deleted_at IS NULL")
public class Especificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idespecificacion")
    private Integer idEspecificacion;
    
    @Column(name = "nombre_especificacion", length = 100, nullable = false)
    private String nombreEspecificacion;
    
    @Column(name = "descripcion_especificacion", length = 255)
    private String descripcionEspecificacion;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
