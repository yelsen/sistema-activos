package pe.edu.unasam.activos.modules.servidores.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.TipoRecurso;

@Entity
@Table(name = "recursos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Recurso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrecurso")
    private Integer idRecurso;
    
    @Column(name = "nombre_recurso", length = 45, nullable = false)
    private String nombreRecurso;
    
    @Column(name = "descripcion_recurso", columnDefinition = "TEXT")
    private String descripcionRecurso;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recurso")
    private TipoRecurso tipoRecurso;
}
