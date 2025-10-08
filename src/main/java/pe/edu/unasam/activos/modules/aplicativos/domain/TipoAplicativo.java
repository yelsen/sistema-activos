package pe.edu.unasam.activos.modules.aplicativos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_aplicativos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class TipoAplicativo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_aplicativo")
    private Integer idTipoAplicativo;
    
    @Column(name = "tipo_aplicativo", length = 100, nullable = false)
    private String tipoAplicativo;
    
    @Column(name = "requiere_licencia", columnDefinition = "TINYINT(1)")
    private Boolean requiereLicencia;
}
