package pe.edu.unasam.activos.modules.proveedores.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoProveedor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "proveedores")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE proveedores SET deleted_at = NOW() WHERE idproveedor = ?")
@SQLRestriction("deleted_at IS NULL")
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproveedor")
    private Integer idProveedor;

    @Column(name = "ruc_proveedor", length = 13, nullable = false)
    private String rucProveedor;
    
    @Column(name = "nombre_proveedor", length = 255, nullable = false)
    private String nombreProveedor;
    
    @Column(name = "direccion_proveedor", columnDefinition = "TEXT")
    private String direccionProveedor;
    
    @Column(name = "telefono_proveedor", length = 20)
    private String telefonoProveedor;
    
    @Column(name = "correo_proveedor", length = 100)
    private String correoProveedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_proveedor")
    private EstadoProveedor estadoProveedor;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
