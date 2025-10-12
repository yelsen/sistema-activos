package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoModulo;

import java.time.LocalDateTime;

@Entity
@Table(name = "modulo_sistemas")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE modulo_sistemas SET deleted_at = NOW(), estado_modulo = 'INACTIVO' WHERE idmodulo_sistemas = ?")
@SQLRestriction("deleted_at IS NULL")
public class ModuloSistema {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmodulo_sistemas")
    private Integer idModuloSistemas;
    
    @Column(name = "nombre_modulo", length = 45, nullable = false)
    private String nombreModulo;
    
    @Column(name = "descripcion_modulo", columnDefinition = "TEXT")
    private String descripcionModulo;
    
    @Column(name = "icono_modulo", length = 45)
    private String iconoModulo;
    
    @Column(name = "ruta_modulo", length = 100)
    private String rutaModulo;
    
    @Column(name = "orden_modulo")
    private Integer ordenModulo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_modulo")
    private EstadoModulo estadoModulo;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;
}
