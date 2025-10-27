package pe.edu.unasam.activos.modules.personas.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoResponsable;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "responsables")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE responsables SET deleted_at = NOW(), estado_responsable = 'INACTIVO' WHERE idresponsable = ?")
@SQLRestriction("deleted_at IS NULL")
public class Responsable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idresponsable")
    private Integer idResponsable;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_dni", referencedColumnName = "dni")
    private Persona persona;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idcargo", referencedColumnName = "idcargo")
    private Cargo cargo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idoficina", referencedColumnName = "idoficina")
    private Oficina oficina;
    
    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;
    
    @Column(name = "fecha_fin_asignacion")
    private LocalDate fechaFinAsignacion;
    
    @Column(name = "es_responsable_principal")
    private boolean esResponsablePrincipal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_responsable")
    private EstadoResponsable estadoResponsable;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
