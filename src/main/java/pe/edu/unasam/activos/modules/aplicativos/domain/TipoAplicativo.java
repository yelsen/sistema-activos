package pe.edu.unasam.activos.modules.aplicativos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_aplicativos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE tipo_aplicativos SET deleted_at = NOW() WHERE idtipo_aplicativo = ?")
@SQLRestriction("deleted_at IS NULL")
public class TipoAplicativo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_aplicativo")
    private Integer idTipoAplicativo;
    
    @Column(name = "tipo_aplicativo", length = 100, nullable = false)
    private String tipoAplicativo;
    
    @Column(name = "requiere_licencia", columnDefinition = "TINYINT(1)")
    private Boolean requiereLicencia;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
