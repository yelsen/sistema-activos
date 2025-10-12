package pe.edu.unasam.activos.modules.mantenimientos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_mantenimientos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE tipo_mantenimientos SET deleted_at = NOW() WHERE idtipo_mantenimiento = ?")
@SQLRestriction("deleted_at IS NULL")
public class TipoMantenimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_mantenimiento")
    private Integer idTipoMantenimiento;
    
    @Column(name = "tipo_mantenimiento", length = 100, nullable = false)
    private String tipoMantenimiento;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
