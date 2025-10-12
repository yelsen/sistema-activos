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
@Table(name = "categoria_equipos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE categoria_equipos SET deleted_at = NOW() WHERE idcategoria_equipos = ?")
@SQLRestriction("deleted_at IS NULL")
public class CategoriaEquipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcategoria_equipos")
    private Integer idCategoriaEquipos;
    
    @Column(name = "categoria_equipo", length = 50, nullable = false)
    private String categoriaEquipo;
    
    @Column(name = "descripcion_categoria_equipo", columnDefinition = "TEXT")
    private String descripcionCategoriaEquipo;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
