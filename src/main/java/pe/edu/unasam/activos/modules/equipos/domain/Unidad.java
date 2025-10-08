package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.TipoUnidad;

@Entity
@Table(name = "unidades")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Unidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idunidad")
    private Integer idUnidad;
    
    @Column(name = "nombre_unidad", length = 50, nullable = false)
    private String nombreUnidad;
    
    @Column(name = "simbolo_unidad", length = 45)
    private String simboloUnidad;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_unidad")
    private TipoUnidad tipoUnidad;
}
