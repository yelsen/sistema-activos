package pe.edu.unasam.activos.modules.personas.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;

import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE usuarios SET deleted_at = NOW(), estado_usuarios = 'INACTIVO' WHERE idusuario = ?")
@SQLRestriction("deleted_at IS NULL")
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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_usuarios")
    private EstadoUsuario estadoUsuarios;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_numero_documento", referencedColumnName = "numero_documento")
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idrol", referencedColumnName = "idrol")
    private Rol rol;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;
}
