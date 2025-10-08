package pe.edu.unasam.activos.modules.activos.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoActivo;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Origen;
import pe.edu.unasam.activos.modules.proveedores.domain.Proveedor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "activos")
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Activo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idactivo")
    private Integer idActivo;
    
    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;
    
    @Column(name = "fecha_fin_garantia")
    private LocalDate fechaFinGarantia;
    
    @Column(name = "costo_activo", precision = 9, scale = 2)
    private BigDecimal costoActivo;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "documento", columnDefinition = "TEXT")
    private String documento;
    
    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_activo")
    private EstadoActivo estadoActivo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idorigen", referencedColumnName = "idorigen")
    private Origen origen;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idoficina", referencedColumnName = "idoficina")
    private Oficina oficina;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idproveedor", referencedColumnName = "idproveedor")
    private Proveedor proveedor;
}
