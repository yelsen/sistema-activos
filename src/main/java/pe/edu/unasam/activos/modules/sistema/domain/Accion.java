package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "acciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Accion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idaccion")
    private Integer idAccion;

    @Column(name = "nombre_accion", nullable = false, length = 50)
    private String nombreAccion;

    @Column(name = "codigo_accion", nullable = false, unique = true, length = 20)
    private String codigoAccion;

    @Column(name = "descripcion_accion")
    private String descripcionAccion;
}