package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.TipoDato;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion_sistemas")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class ConfiguracionSistema {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idconfiguracion_sistema")
    private Integer idConfiguracionSistema;
    
    @Column(name = "clave_config", length = 100, nullable = false, unique = true)
    private String claveConfig;
    
    @Column(name = "valor_config", columnDefinition = "TEXT")
    private String valorConfig;
    
    @Column(name = "descripcion_config", columnDefinition = "TEXT")
    private String descripcionConfig;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dato")
    private TipoDato tipoDato;
    
    @Column(name = "categoria_config", length = 45)
    private String categoriaConfig;
    
    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;
}
