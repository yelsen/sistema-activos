package pe.edu.unasam.activos.modules.ubicaciones.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoOficina;

@Entity
@Table(name = "oficinas")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Oficina {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idoficina")
    private Integer idOficina;
    
    @Column(name = "nombre_oficina", length = 255, nullable = false)
    private String nombreOficina;
    
    @Column(name = "direccion_oficina", length = 255)
    private String direccionOficina;
    
    @Column(name = "telefono_oficina", length = 20)
    private String telefonoOficina;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_oficina")
    private EstadoOficina estadoOficina;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idtipo_oficina", referencedColumnName = "idtipo_oficina")
    private TipoOficina tipoOficina;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_iddepartamento", referencedColumnName = "iddepartamento")
    private Departamento departamento;
}
