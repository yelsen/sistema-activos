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
@Table(name = "componentes")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE componentes SET deleted_at = NOW() WHERE idcomponente = ?")
@SQLRestriction("deleted_at IS NULL")
public class Componente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomponente")
    private Integer idComponente;
    
    @Column(name = "nombre_componente", length = 100, nullable = false)
    private String nombreComponente;
    
    @Column(name = "descripcion_componente", length = 255)
    private String descripcionComponente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idcategoria_componente", referencedColumnName = "idcategoria_componente")
    private CategoriaComponente categoriaComponente;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
