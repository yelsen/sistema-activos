package pe.edu.unasam.activos.modules.aplicativos.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.EstadoInstalacion;
import pe.edu.unasam.activos.modules.servidores.domain.Servidor;

@Entity
@Table(name = "servidor_aplicativos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@IdClass(ServidorAplicativoId.class)
public class ServidorAplicativo {
   
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idservidor", referencedColumnName = "idservidor")
    private Servidor servidor;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idaplicativo", referencedColumnName = "idaplicativo")
    private Aplicativo aplicativo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_instalacion")
    private EstadoInstalacion estadoInstalacion;
}

