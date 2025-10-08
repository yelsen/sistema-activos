package pe.edu.unasam.activos.audit.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.AccionAuditoria;

@Entity
@Table(name = "auditoria_logs")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class AuditoriaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idauditoria_log")
    private Integer idAuditoriaLog;
    
    @Column(name = "tabla_afectada", length = 45)
    private String tablaAfectada;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "accion")
    private AccionAuditoria accion;
    
    @Column(name = "valores_anteriores", columnDefinition = "JSON")
    private String valoresAnteriores;
    
    @Column(name = "valores_nuevos", columnDefinition = "JSON")
    private String valoresNuevos;
}
