package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipo_especificaciones")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@IdClass(EquipoEspecificacionId.class)
public class EquipoEspecificacion {
    
    @Column(name = "valor_especificacion", length = 255)
    private String valorEspecificacion;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idequipo", referencedColumnName = "idequipo")
    private Equipo equipo;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idespecificacion", referencedColumnName = "idespecificacion")
    private Especificacion especificacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idunidad", referencedColumnName = "idunidad")
    private Unidad unidad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idcategoria_componente", referencedColumnName = "idcategoria_componente")
    private CategoriaComponente categoriaComponente;
}
