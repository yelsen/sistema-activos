package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_componentes")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class CategoriaComponente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcategoria_componente")
    private Integer idCategoriaComponente;
    
    @Column(name = "categoria_componente", length = 45, nullable = false)
    private String categoriaComponente;
}
