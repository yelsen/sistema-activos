package pe.edu.unasam.activos.modules.ubicaciones.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departamentos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Departamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddepartamento")
    private Integer idDepartamento;
    
    @Column(name = "nombre_departamento", length = 255, nullable = false)
    private String nombreDepartamento;
    
    @Column(name = "direccion_departamento", length = 255)
    private String direccionDepartamento;
}
