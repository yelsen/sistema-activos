package pe.edu.unasam.activos.modules.personas.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_documentos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE tipo_documentos SET deleted_at = NOW() WHERE idtipo_documento = ?")
@SQLRestriction("deleted_at IS NULL")
public class TipoDocumento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_documento")
    private Integer idTipoDocumento;
    
    @Column(name = "tipo_documento", length = 45, nullable = false)
    private String tipoDocumento;
    
    @Column(name = "longitud_minima", length = 8)
    private String longitudMinima;
    
    @Column(name = "longitud_maxima", length = 12)
    private String longitudMaxima;
    
    @Column(name = "patron_validacion", length = 100)
    private String patronValidacion;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
