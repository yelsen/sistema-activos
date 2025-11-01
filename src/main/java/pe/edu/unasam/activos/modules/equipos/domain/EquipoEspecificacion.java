package pe.edu.unasam.activos.modules.equipos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipo_especificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(EquipoEspecificacionId.class)
public class EquipoEspecificacion {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idequipo", referencedColumnName = "idequipo")
    private Equipo equipo;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idcomponente", referencedColumnName = "idcomponente")
    private Componente componente;

    @Column(name = "valor_especificacion", length = 255)
    private String valorEspecificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idunidad", referencedColumnName = "idunidad")
    private Unidad unidad;
}
