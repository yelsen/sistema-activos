package pe.edu.unasam.activos.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class LogoutRequest {
    
    @NotBlank(message = "El token es obligatorio")
    private String token;
}
