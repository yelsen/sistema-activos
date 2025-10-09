package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoSesion;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesion_usuarios")
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
    
    @Column(name = "token_sesion", length = 255, nullable = false)
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
}
