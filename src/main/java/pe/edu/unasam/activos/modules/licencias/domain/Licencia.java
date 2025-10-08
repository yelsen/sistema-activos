package pe.edu.unasam.activos.modules.licencias.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoLicencia;
import pe.edu.unasam.activos.modules.aplicativos.domain.Aplicativo;
import pe.edu.unasam.activos.modules.proveedores.domain.Proveedor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "licencias")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Licencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlicencia")
    private Integer idLicencia;
    
    @Column(name = "clave_licencia", length = 100)
    private String claveLicencia;
    
    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;
    
    @Column(name = "fecha_expiracion")
    private LocalDate fechaExpiracion;
    
    @Column(name = "cantidad_licencia")
    private Integer cantidadLicencia;
    
    @Column(name = "precio_licencia", precision = 9, scale = 2)
    private BigDecimal precioLicencia;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_licencia")
    private EstadoLicencia estadoLicencia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idaplicativo", referencedColumnName = "idaplicativo")
    private Aplicativo aplicativo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idtipo_licencia", referencedColumnName = "idtipo_licencia")
    private TipoLicencia tipoLicencia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idproveedor", referencedColumnName = "idproveedor")
    private Proveedor proveedor;
}
