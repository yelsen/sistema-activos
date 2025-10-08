package pe.edu.unasam.activos.modules.apis.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.UsoApi;
import pe.edu.unasam.activos.common.enums.EstadoIntegracion;
import pe.edu.unasam.activos.modules.aplicativos.domain.Aplicativo;

@Entity
@Table(name = "api_aplicativos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@IdClass(ApiAplicativoId.class)
public class ApiAplicativo {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idaplicativo", referencedColumnName = "idaplicativo")
    private Aplicativo aplicativo;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idapi", referencedColumnName = "idapi")
    private Api api;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "uso")
    private UsoApi uso;
    
    @Column(name = "version_integracion", length = 45)
    private String versionIntegracion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_integracion")
    private EstadoIntegracion estadoIntegracion;
}
