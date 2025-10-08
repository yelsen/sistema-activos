package pe.edu.unasam.activos.modules.sistema.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.TipoDato;

@Entity
@Table(name = "configuracion_sistemas")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class ConfiguracionSistema {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idconfiguracion_sistema")
    private Integer idConfiguracionSistema;
    
    @Column(name = "clave_config", length = 100, nullable = false, unique = true)
    private String claveConfig;
    
    @Column(name = "valor_config", columnDefinition = "TEXT")
    private String valorConfig;
    
    @Column(name = "descripcion_config", columnDefinition = "TEXT")
    private String descripcionConfig;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dato")
    private TipoDato tipoDato;
    
    @Column(name = "categoria_config", length = 45)
    private String categoriaConfig;
    
    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;
}
