package pe.edu.unasam.activos.modules.ubicaciones.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "origenes")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Origen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idorigen")
    private Integer idOrigen;
    
    @Column(name = "nombre_origen", length = 45, nullable = false)
    private String nombreOrigen;
}
