package pe.edu.unasam.activos.modules.personas.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cargos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Cargo {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcargo")
    private Integer idCargo;
    
    @Column(name = "nombre_cargo", length = 100, nullable = false)
    private String nombreCargo;
}
