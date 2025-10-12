package pe.edu.unasam.activos.modules.licencias.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoLicencia;
import pe.edu.unasam.activos.modules.aplicativos.domain.Aplicativo;
import pe.edu.unasam.activos.modules.proveedores.domain.Proveedor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "licencias")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE licencias SET deleted_at = NOW(), estado_licencia = 'INACTIVO' WHERE idlicencia = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
