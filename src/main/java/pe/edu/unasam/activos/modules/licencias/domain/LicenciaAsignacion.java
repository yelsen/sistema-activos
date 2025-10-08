package pe.edu.unasam.activos.modules.licencias.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoAsignacion;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import java.time.LocalDate;

@Entity
@Table(name = "licencia_asignaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@IdClass(LicenciaAsignacionId.class)
public class LicenciaAsignacion {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idlicencia", referencedColumnName = "idlicencia")
    private Licencia licencia;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idoficina", referencedColumnName = "idoficina")
    private Oficina oficina;
    
    @Column(name = "cantidad_asignada")
    private Integer cantidadAsignada;
    
    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;
    
    @Column(name = "fecha_liberacion")
    private LocalDate fechaLiberacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asignacion")
    private EstadoAsignacion estadoAsignacion;
}
