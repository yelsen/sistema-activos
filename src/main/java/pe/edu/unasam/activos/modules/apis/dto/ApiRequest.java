package pe.edu.unasam.activos.modules.apis.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.MetodoAutenticacion;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class ApiRequest {
    
    @NotBlank(message = "El nombre de la API es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreApi;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcionApi;
    
    @NotBlank(message = "La URL de la API es obligatoria")
    @Size(max = 255, message = "La URL no puede exceder 255 caracteres")
    @Pattern(regexp = "^https?://.*", message = "La URL debe comenzar con http:// o https://")
    private String urlApi;
    
    @Size(max = 45, message = "La versión no puede exceder 45 caracteres")
    private String versionApi;
    
    @NotNull(message = "El método de autenticación es obligatorio")
    private MetodoAutenticacion metodoAutenticacion;
}
