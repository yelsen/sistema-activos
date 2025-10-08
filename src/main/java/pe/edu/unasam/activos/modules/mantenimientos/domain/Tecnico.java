package pe.edu.unasam.activos.modules.mantenimientos.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.modules.personas.domain.Persona;

@Entity
@Table(name = "tecnicos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Tecnico {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtecnico")
    private Integer idTecnico;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_documento", referencedColumnName = "documento")
    private Persona persona;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idespecialidad", referencedColumnName = "idespecialidad")
    private Especialidad especialidad;
}
