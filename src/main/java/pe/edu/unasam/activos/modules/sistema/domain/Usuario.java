package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;
    
    @Column(name = "usuario", length = 100, nullable = false, unique = true)
    private String usuario;
    
    @Column(name = "contrasena", columnDefinition = "TEXT", nullable = false)
    private String contrasena;
    
    @Column(name = "salt", length = 100)
    private String salt;
    
    @Column(name = "ultimo_acceso")
    private LocalTime ultimoAcceso;
    
    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos;
    
    @Column(name = "bloqueado_hasta")
    private LocalDateTime bloqueadoHasta;
    
    @Column(name = "debe_cambiar_password", columnDefinition = "TINYINT(1)")
    private Boolean debeCambiarPassword;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_usuarios")
    private EstadoUsuario estadoUsuarios;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_documento", referencedColumnName = "documento")
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idrol", referencedColumnName = "idrol")
    private Rol rol;
}
