package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modelos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Modelo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmodelo")
    private Integer idModelo;
    
    @Column(name = "nombre_modelo", length = 100, nullable = false)
    private String nombreModelo;
    
    @Column(name = "descripcion_modelo", length = 150)
    private String descripcionModelo;
    
    @Column(name = "ano_lanzamiento", columnDefinition = "YEAR")
    private Integer anoLanzamiento;
}
