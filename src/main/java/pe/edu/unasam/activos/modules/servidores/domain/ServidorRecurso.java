package pe.edu.unasam.activos.modules.servidores.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.modules.equipos.domain.Unidad;

@Entity
@Table(name = "servidor_recursos")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@IdClass(ServidorRecursoId.class)
public class ServidorRecurso {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idservidor", referencedColumnName = "idservidor")
    private Servidor servidor;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idrecurso", referencedColumnName = "idrecurso")
    private Recurso recurso;
    
    @Column(name = "valor", length = 100)
    private String valor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idunidad", referencedColumnName = "idunidad")
    private Unidad unidad;
}
