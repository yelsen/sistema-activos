package pe.edu.unasam.activos.modules.apis.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.UsoApi;
import pe.edu.unasam.activos.common.enums.EstadoIntegracion;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class ApiIntegracionDto {
    
    @NotNull(message = "El ID del aplicativo es obligatorio")
    @Positive(message = "El ID del aplicativo debe ser positivo")
    private Integer idAplicativo;
    
    @NotNull(message = "El ID de la API es obligatorio")
    @Positive(message = "El ID de la API debe ser positivo")
    private Integer idApi;
    
    @NotNull(message = "El uso de la API es obligatorio")
    private UsoApi uso;
    
    @Size(max = 45, message = "La versión de integración no puede exceder 45 caracteres")
    private String versionIntegracion;
    
    @NotNull(message = "El estado de integración es obligatorio")
    private EstadoIntegracion estadoIntegracion;
    
    // Información adicional para la respuesta
    private String nombreAplicativo;
    private String nombreApi;
    private String urlApi;
}
