package pe.edu.unasam.activos.modules.personas.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_documentos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class TipoDocumento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_documento")
    private Integer idTipoDocumento;
    
    @Column(name = "tipo_documento", length = 45, nullable = false)
    private String tipoDocumento;
    
    @Column(name = "longitud_minima", length = 8)
    private String longitudMinima;
    
    @Column(name = "longitud_maxima", length = 12)
    private String longitudMaxima;
    
    @Column(name = "patron_validacion", length = 100)
    private String patronValidacion;
}
