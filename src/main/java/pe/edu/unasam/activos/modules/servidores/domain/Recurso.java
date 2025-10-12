package pe.edu.unasam.activos.modules.servidores.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.TipoRecurso;

import java.time.LocalDateTime;

@Entity
@Table(name = "recursos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE recursos SET deleted_at = NOW() WHERE idrecurso = ?")
@SQLRestriction("deleted_at IS NULL")
public class Recurso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrecurso")
    private Integer idRecurso;
    
    @Column(name = "nombre_recurso", length = 45, nullable = false)
    private String nombreRecurso;
    
    @Column(name = "descripcion_recurso", columnDefinition = "TEXT")
    private String descripcionRecurso;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recurso")
    private TipoRecurso tipoRecurso;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
