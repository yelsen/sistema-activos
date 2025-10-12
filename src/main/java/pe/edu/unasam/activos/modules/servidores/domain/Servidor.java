package pe.edu.unasam.activos.modules.servidores.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.TipoServidor;

import java.time.LocalDateTime;

@Entity
@Table(name = "servidores")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE servidores SET deleted_at = NOW(), tipo_servidor = 'INACTIVO' WHERE idservidor = ?")
@SQLRestriction("deleted_at IS NULL")
public class Servidor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idservidor")
    private Integer idServidor;
    
    @Column(name = "nombre_servidor", length = 100, nullable = false)
    private String nombreServidor;
    
    @Column(name = "hosting", length = 255)
    private String hosting;
    
    @Column(name = "direccion_ip", length = 45)
    private String direccionIp;
    
    @Column(name = "puerto_principal")
    private Integer puertoPrincipal;
    
    @Column(name = "usuario_admin", length = 100)
    private String usuarioAdmin;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servidor")
    private TipoServidor tipoServidor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idrol_servidor", referencedColumnName = "idrol_servidor")
    private RolServidor rolServidor;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
