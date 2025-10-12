package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.TipoUnidad;

import java.time.LocalDateTime;

@Entity
@Table(name = "unidades")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE unidades SET deleted_at = NOW() WHERE idunidad = ?")
@SQLRestriction("deleted_at IS NULL")
public class Unidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idunidad")
    private Integer idUnidad;
    
    @Column(name = "nombre_unidad", length = 50, nullable = false)
    private String nombreUnidad;
    
    @Column(name = "simbolo_unidad", length = 45)
    private String simboloUnidad;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_unidad")
    private TipoUnidad tipoUnidad;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
