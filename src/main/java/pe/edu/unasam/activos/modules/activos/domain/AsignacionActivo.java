package pe.edu.unasam.activos.modules.activos.domain;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoAsignacion;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;

@Entity
@Table(name = "asignacion_activos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(AsignacionActivoId.class)
public class AsignacionActivo {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idactivo", referencedColumnName = "idactivo")
    private Activo activo;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idoficina", referencedColumnName = "idoficina")
    private Oficina oficina;

    @Column(name = "cantidad_asignacion")
    private Integer cantidadAsignacion;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asignacion")
    private EstadoAsignacion estadoAsignacion;
}
