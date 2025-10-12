package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoEquipo;
import pe.edu.unasam.activos.modules.activos.domain.Activo;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE equipos SET deleted_at = NOW(), estado_equipo = 'INACTIVO' WHERE idequipo = ?")
@SQLRestriction("deleted_at IS NULL")
public class Equipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idequipo")
    private Integer idEquipo;
    
    @Column(name = "codigo_equipo", length = 50, unique = true)
    private String codigoEquipo;
    
    @Column(name = "num_serie", length = 100)
    private String numSerie;
    
    @Column(name = "detalle_equipo", columnDefinition = "TEXT")
    private String detalleEquipo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_equipo")
    private EstadoEquipo estadoEquipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idactivo", referencedColumnName = "idactivo")
    private Activo activo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idcategoria_equipos", referencedColumnName = "idcategoria_equipos")
    private CategoriaEquipo categoriaEquipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idmodelo", referencedColumnName = "idmodelo")
    private Modelo modelo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idmarca", referencedColumnName = "idmarca")
    private Marca marca;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
