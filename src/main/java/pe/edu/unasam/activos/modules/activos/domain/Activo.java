package pe.edu.unasam.activos.modules.activos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoActivo;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Origen;
import pe.edu.unasam.activos.modules.proveedores.domain.Proveedor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "activos")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE activos SET deleted_at = NOW(), estado_activo = 'INACTIVO' WHERE idactivo = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
