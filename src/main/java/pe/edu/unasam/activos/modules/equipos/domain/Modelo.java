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
@Table(name = "modelos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE modelos SET deleted_at = NOW() WHERE idmodelo = ?")
@SQLRestriction("deleted_at IS NULL")
public class Modelo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmodelo")
    private Integer idModelo;
    
    @Column(name = "nombre_modelo", length = 100, nullable = false)
    private String nombreModelo;
    
    @Column(name = "descripcion_modelo", length = 150)
    private String descripcionModelo;
    
    @Column(name = "ano_lanzamiento", columnDefinition = "YEAR")
    private Integer anoLanzamiento;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
