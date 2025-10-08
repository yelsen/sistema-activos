package pe.edu.unasam.activos.modules.licencias.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_licencias")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class TipoLicencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_licencia")
    private Integer idTipoLicencia;
    
    @Column(name = "tipo_licencia", length = 45, nullable = false)
    private String tipoLicencia;
    
    @Column(name = "descripcion_tipo_licencia", columnDefinition = "TEXT")
    private String descripcionTipoLicencia;
    
    @Column(name = "requiere_renovacion", columnDefinition = "TINYINT(1)")
    private Boolean requiereRenovacion;
}
