package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoRol;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE roles SET deleted_at = NOW(), estado_rol = 'INACTIVO' WHERE idrol = ?")
@SQLRestriction("deleted_at IS NULL")
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol")
    private Integer idRol;
    
    @Column(name = "nombre_rol", length = 100, nullable = false, unique = true)
    private String nombreRol;

    @Column(name = "descripcion_rol", columnDefinition = "TEXT")
    private String descripcionRol;
    
    @Column(name = "nivel_acceso")
    private Integer nivelAcceso;
    
    @Column(name = "color_rol", length = 7)
    private String colorRol;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_rol")
    private EstadoRol estadoRol;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;
}
