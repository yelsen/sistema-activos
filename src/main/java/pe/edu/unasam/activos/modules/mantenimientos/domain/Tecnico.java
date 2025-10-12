package pe.edu.unasam.activos.modules.mantenimientos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.modules.personas.domain.Persona;

import java.time.LocalDateTime;

@Entity
@Table(name = "tecnicos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE tecnicos SET deleted_at = NOW() WHERE idtecnico = ?")
@SQLRestriction("deleted_at IS NULL")
public class Tecnico {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtecnico")
    private Integer idTecnico;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_documento", referencedColumnName = "documento")
    private Persona persona;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idespecialidad", referencedColumnName = "idespecialidad")
    private Especialidad especialidad;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
