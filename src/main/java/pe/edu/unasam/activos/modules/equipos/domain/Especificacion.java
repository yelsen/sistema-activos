package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especificaciones")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Especificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idespecificacion")
    private Integer idEspecificacion;
    
    @Column(name = "nombre_especificacion", length = 100, nullable = false)
    private String nombreEspecificacion;
    
    @Column(name = "descripcion_especificacion", length = 255)
    private String descripcionEspecificacion;
}
