package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoEquipo;
import pe.edu.unasam.activos.modules.activos.domain.Activo;

@Entity
@Table(name = "equipos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Equipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idequipo")
    private Integer idEquipo;
    
    @Column(name = "codigo_equipo", length = 50, unique = true)
    private String codigoEquipo;
    
    @Column(name = "num_serie", length = 100)
    private String numSerie;
    
    @Column(name = "detalle_equipo", columnDefinition = "TEXT")
    private String detalleEquipo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_equipo")
    private EstadoEquipo estadoEquipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idactivo", referencedColumnName = "idactivo")
    private Activo activo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idcategoria_equipos", referencedColumnName = "idcategoria_equipos")
    private CategoriaEquipo categoriaEquipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idmodelo", referencedColumnName = "idmodelo")
    private Modelo modelo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idmarca", referencedColumnName = "idmarca")
    private Marca marca;
}
