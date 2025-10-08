package pe.edu.unasam.activos.modules.auth.dto;
import lombok.*;
import java.util.List;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class UserInfoResponse {
    
    private Integer idUsuario;
    private String usuario;
    private String nombreCompleto;
    private String email;
    private String documento;
    private List<String> roles;
    private List<String> permisos;
    private Boolean debeCambiarPassword;   
}
