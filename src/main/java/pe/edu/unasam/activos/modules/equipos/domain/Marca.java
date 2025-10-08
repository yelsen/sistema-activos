package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "marcas")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Marca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmarca")
    private Integer idMarca;
    
    @Column(name = "nombre_marca", length = 100, nullable = false)
    private String nombreMarca;
    
    @Column(name = "descripcion_marca", length = 150)
    private String descripcionMarca;
}
