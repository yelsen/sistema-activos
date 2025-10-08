package pe.edu.unasam.activos.modules.auth.dto;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class TokenResponse {
    
    private String token;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserInfoResponse userInfo;
}
