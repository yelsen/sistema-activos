package pe.edu.unasam.activos.modules.aplicativos.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoInstalacion;
import pe.edu.unasam.activos.modules.equipos.domain.Equipo;
import java.time.LocalDate;

@Entity
@Table(name = "instalacion_aplicativos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@IdClass(InstalacionAplicativoId.class)
public class InstalacionAplicativo {
   
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idequipo", referencedColumnName = "idequipo")
    private Equipo equipo;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idaplicativo", referencedColumnName = "idaplicativo")
    private Aplicativo aplicativo;
    
    @Column(name = "version_instalacion", length = 45)
    private String versionInstalacion;
    
    @Column(name = "fecha_instalacion")
    private LocalDate fechaInstalacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_instalacion")
    private EstadoInstalacion estadoInstalacion;
}
