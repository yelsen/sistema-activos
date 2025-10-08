package pe.edu.unasam.activos.modules.servidores.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.TipoServidor;

@Entity
@Table(name = "servidores")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
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
}
