package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoModulo;

@Entity
@Table(name = "modulo_sistemas")
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor 
@Builder
public class ModuloSistema {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmodulo_sistemas")
    private Integer idModuloSistemas;
    
    @Column(name = "nombre_modulo", length = 45, nullable = false)
    private String nombreModulo;
    
    @Column(name = "descripcion_modulo", columnDefinition = "TEXT")
    private String descripcionModulo;
    
    @Column(name = "icono_modulo", length = 45)
    private String iconoModulo;
    
    @Column(name = "ruta_modulo", length = 100)
    private String rutaModulo;
    
    @Column(name = "orden_modulo")
    private Integer ordenModulo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_modulo")
    private EstadoModulo estadoModulo;
}
