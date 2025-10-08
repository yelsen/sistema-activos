package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_equipos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class CategoriaEquipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcategoria_equipos")
    private Integer idCategoriaEquipos;
    
    @Column(name = "categoria_equipo", length = 50, nullable = false)
    private String categoriaEquipo;
    
    @Column(name = "descripcion_categoria_equipo", columnDefinition = "TEXT")
    private String descripcionCategoriaEquipo;
}
