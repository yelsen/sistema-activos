package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoSesion;
import pe.edu.unasam.activos.modules.personas.domain.Usuario;

import java.time.LocalDateTime;

@Entity
@Table(name = "sesion_usuarios")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class SesionUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsesion_usuario")
    private Integer idSesionUsuario;
    
    @Column(name = "token_sesion", columnDefinition = "TEXT", nullable = false)
    private String tokenSesion;
    
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_ultimo_acceso")
    private LocalDateTime fechaUltimoAcceso;
    
    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_sesion")
    private EstadoSesion estadoSesion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idusuario", referencedColumnName = "idusuario")
    private Usuario usuario;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;
}
