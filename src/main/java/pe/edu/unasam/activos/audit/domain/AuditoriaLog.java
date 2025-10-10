package pe.edu.unasam.activos.audit.domain;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.AccionAuditoria;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_idusuario", referencedColumnName = "idusuario")
    private Usuario usuario;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "detalles", columnDefinition = "TEXT")
    private String detalles;
}
