package pe.edu.unasam.activos.modules.mantenimientos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_mantenimientos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class TipoMantenimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_mantenimiento")
    private Integer idTipoMantenimiento;
    
    @Column(name = "tipo_mantenimiento", length = 100, nullable = false)
    private String tipoMantenimiento;
}
