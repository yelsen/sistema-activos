package pe.edu.unasam.activos.modules.servidores.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.NivelCriticidad;

import java.time.LocalDateTime;

@Entity
@Table(name = "rol_servidores")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE rol_servidores SET deleted_at = NOW() WHERE idrol_servidor = ?")
@SQLRestriction("deleted_at IS NULL")
public class RolServidor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol_servidor")
    private Integer idRolServidor;
    
    @Column(name = "rol_servidor", length = 100, nullable = false)
    private String rolServidor;
    
    @Column(name = "descripcion_rol", columnDefinition = "TEXT")
    private String descripcionRol;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_criticidad")
    private NivelCriticidad nivelCriticidad;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
