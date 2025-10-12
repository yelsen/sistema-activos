package pe.edu.unasam.activos.modules.ubicaciones.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoOficina;

import java.time.LocalDateTime;

@Entity
@Table(name = "oficinas")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE oficinas SET deleted_at = NOW(), estado_oficina = 'INACTIVO' WHERE idoficina = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
