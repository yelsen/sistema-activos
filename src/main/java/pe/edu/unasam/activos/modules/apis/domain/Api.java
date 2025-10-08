package pe.edu.unasam.activos.modules.apis.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.MetodoAutenticacion;

@Entity
@Table(name = "apis")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Api {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idapi")
    private Integer idApi;
    
    @Column(name = "nombre_api", length = 100, nullable = false)
    private String nombreApi;
    
    @Column(name = "descripcion_api", columnDefinition = "TEXT")
    private String descripcionApi;
    
    @Column(name = "url_api", length = 255)
    private String urlApi;
    
    @Column(name = "version_api", length = 45)
    private String versionApi;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_autenticacion")
    private MetodoAutenticacion metodoAutenticacion;
}
