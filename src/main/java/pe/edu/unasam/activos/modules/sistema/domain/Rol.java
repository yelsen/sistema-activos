package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoRol;

@Entity
@Table(name = "roles")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
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
}
