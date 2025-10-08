package pe.edu.unasam.activos.modules.servidores.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.NivelCriticidad;

@Entity
@Table(name = "rol_servidores")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
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
}
