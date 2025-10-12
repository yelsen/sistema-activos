package pe.edu.unasam.activos.modules.mantenimientos.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.unasam.activos.common.enums.EstadoMantenimiento;
import pe.edu.unasam.activos.modules.activos.domain.Activo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;


@Entity
@Table(name = "mantenimientos")
@EntityListeners(AuditingEntityListener.class)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@SQLDelete(sql = "UPDATE mantenimientos SET deleted_at = NOW(), estado_mantenimiento = 'CANCELADO' WHERE idmantenimiento = ?")
@SQLRestriction("deleted_at IS NULL")
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmantenimiento")
    private Integer idMantenimiento;
    
    @Column(name = "problema_reportado", length = 255)
    private String problemaReportado;
    
    @Column(name = "descripcion_mantenimiento", columnDefinition = "TEXT")
    private String descripcionMantenimiento;
    
    @Column(name = "fecha_recibido")
    private LocalDate fechaRecibido;
    
    @Column(name = "fecha_programada")
    private LocalDate fechaProgramada;
    
    @Column(name = "fecha_inicio_mantenimiento")
    private LocalDate fechaInicioMantenimiento;
    
    @Column(name = "fecha_fin_mantenimiento")
    private LocalDate fechaFinMantenimiento;
    
    @Column(name = "tiempo_inactividad")
    private LocalTime tiempoInactividad;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_mantenimiento")
    private EstadoMantenimiento estadoMantenimiento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idtipo_mantenimiento", referencedColumnName = "idtipo_mantenimiento")
    private TipoMantenimiento tipoMantenimiento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idactivo", referencedColumnName = "idactivo")
    private Activo activo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idtecnico", referencedColumnName = "idtecnico")
    private Tecnico tecnico;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
