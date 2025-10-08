package pe.edu.unasam.activos.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class LoginRequest {
    
    @NotBlank(message = "El usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    private Boolean recordarme;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
