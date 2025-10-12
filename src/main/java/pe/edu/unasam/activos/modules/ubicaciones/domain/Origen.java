package pe.edu.unasam.activos.modules.ubicaciones.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "origenes")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE origenes SET deleted_at = NOW() WHERE idorigen = ?")
@SQLRestriction("deleted_at IS NULL")
public class Origen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idorigen")
    private Integer idOrigen;
    
    @Column(name = "nombre_origen", length = 45, nullable = false)
    private String nombreOrigen;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
