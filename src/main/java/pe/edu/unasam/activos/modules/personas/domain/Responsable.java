package pe.edu.unasam.activos.modules.personas.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoResponsable;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import java.time.LocalDate;

@Entity
@Table(name = "responsables")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Responsable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idresponsable")
    private Integer idResponsable;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_documento", referencedColumnName = "documento")
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
    
    @Column(name = "es_responsable_principal", columnDefinition = "TINYINT(1)")
    private Boolean esResponsablePrincipal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_responsable")
    private EstadoResponsable estadoResponsable;
}
