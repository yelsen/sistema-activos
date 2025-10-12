package pe.edu.unasam.activos.modules.personas.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.Genero;
import pe.edu.unasam.activos.common.enums.EstadoPersona;

import java.time.LocalDateTime;

@Entity
@Table(name = "personas")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE personas SET deleted_at = NOW(), estado = 'INACTIVO' WHERE documento = ?")
@SQLRestriction("deleted_at IS NULL")
public class Persona {
    
    @Id
    @Column(name = "documento", length = 20)
    private String documento;
    
    @Column(name = "apellidos", length = 100, nullable = false)
    private String apellidos;
    
    @Column(name = "nombres", length = 100, nullable = false)
    private String nombres;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "direccion", length = 200)
    private String direccion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private Genero genero;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPersona estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idtipo_documento", referencedColumnName = "idtipo_documento")
    private TipoDocumento tipoDocumento;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
