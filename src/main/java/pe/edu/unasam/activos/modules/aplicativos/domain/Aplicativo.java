package pe.edu.unasam.activos.modules.aplicativos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoAplicativo;
import pe.edu.unasam.activos.modules.activos.domain.Activo;

import java.time.LocalDateTime;

@Entity
@Table(name = "aplicativos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE aplicativos SET deleted_at = NOW(), estado_aplicativo = 'INACTIVO' WHERE idaplicativo = ?")
@SQLRestriction("deleted_at IS NULL")
public class Aplicativo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idaplicativo")
    private Integer idAplicativo;
    
    @Column(name = "nombre_aplicativo", length = 255, nullable = false)
    private String nombreAplicativo;
    
    @Column(name = "descripcion_aplicativo", columnDefinition = "TEXT")
    private String descripcionAplicativo;
    
    @Column(name = "url_aplicativo", length = 45)
    private String urlAplicativo;
    
    @Column(name = "version_actual", length = 45)
    private String versionActual;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_aplicativo")
    private EstadoAplicativo estadoAplicativo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idtipo_aplicativo", referencedColumnName = "idtipo_aplicativo")
    private TipoAplicativo tipoAplicativo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idactivo", referencedColumnName = "idactivo")
    private Activo activo;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
