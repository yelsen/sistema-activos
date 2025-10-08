package pe.edu.unasam.activos.modules.ubicaciones.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_oficinas")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class TipoOficina {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_oficina")
    private Integer idTipoOficina;
    
    @Column(name = "tipo_oficina", length = 100, nullable = false)
    private String tipoOficina;
}
