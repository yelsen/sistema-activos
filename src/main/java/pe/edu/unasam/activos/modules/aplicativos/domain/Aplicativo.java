package pe.edu.unasam.activos.modules.aplicativos.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoAplicativo;
import pe.edu.unasam.activos.modules.activos.domain.Activo;

@Entity
@Table(name = "aplicativos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Aplicativo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idaplicativo")
    private Integer idAplicativo;
    
    @Column(name = "nombre_aplicativo", length = 255, nullable = false)
    private String nombreAplicativo;
    
    @Column(name = "descripcion_aplicativo", columnDefinition = "TEXT")
    private String descripcionAplicativo;
    
    @Column(name = "url_aplicativo", length = 45)
    private String urlAplicativo;
    
    @Column(name = "version_actual", length = 45)
    private String versionActual;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_aplicativo")
    private EstadoAplicativo estadoAplicativo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idtipo_aplicativo", referencedColumnName = "idtipo_aplicativo")
    private TipoAplicativo tipoAplicativo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idactivo", referencedColumnName = "idactivo")
    private Activo activo;
}
