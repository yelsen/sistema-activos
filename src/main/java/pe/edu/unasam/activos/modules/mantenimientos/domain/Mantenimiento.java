package pe.edu.unasam.activos.modules.mantenimientos.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoMantenimiento;
import pe.edu.unasam.activos.modules.activos.domain.Activo;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "mantenimientos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
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
}
