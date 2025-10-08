package pe.edu.unasam.activos.modules.proveedores.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proveedores")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproveedor")
    private Integer idProveedor;
    
    @Column(name = "nombre_proveedor", length = 255, nullable = false)
    private String nombreProveedor;
}
