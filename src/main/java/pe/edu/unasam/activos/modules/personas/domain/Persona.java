package pe.edu.unasam.activos.modules.personas.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.Genero;
import pe.edu.unasam.activos.common.enums.EstadoPersona;

@Entity
@Table(name = "personas")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Persona {
    
    @Id
    @Column(name = "documento", length = 20)
    private String documento;
    
    @Column(name = "apellidos", length = 100, nullable = false)
    private String apellidos;
    
    @Column(name = "nombres", length = 100, nullable = false)
    private String nombres;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "direccion", length = 200)
    private String direccion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private Genero genero;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPersona estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idtipo_documento", referencedColumnName = "idtipo_documento")
    private TipoDocumento tipoDocumento;
}
