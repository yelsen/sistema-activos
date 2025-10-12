package pe.edu.unasam.activos.modules.licencias.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_licencias")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE tipo_licencias SET deleted_at = NOW() WHERE idtipo_licencia = ?")
@SQLRestriction("deleted_at IS NULL")
public class TipoLicencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_licencia")
    private Integer idTipoLicencia;
    
    @Column(name = "tipo_licencia", length = 45, nullable = false)
    private String tipoLicencia;
    
    @Column(name = "descripcion_tipo_licencia", columnDefinition = "TEXT")
    private String descripcionTipoLicencia;
    
    @Column(name = "requiere_renovacion", columnDefinition = "TINYINT(1)")
    private Boolean requiereRenovacion;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
