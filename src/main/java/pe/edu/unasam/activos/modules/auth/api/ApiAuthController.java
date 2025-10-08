package pe.edu.unasam.activos.modules.auth.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.common.dto.ApiResponse;
import pe.edu.unasam.activos.modules.auth.dto.*;
import pe.edu.unasam.activos.modules.auth.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class ApiAuthController {

    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        
        TokenResponse tokenResponse = authService.login(request);
        
        ApiResponse<TokenResponse> response = ApiResponse.<TokenResponse>builder()
                .success(true)
                .message("Login exitoso")
                .data(tokenResponse)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
        log.info("API: Logout request");
        
        authService.logout(request.getToken());
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Logout exitoso")
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser(Authentication authentication) {
        log.info("API: Getting current user info");
        
        String username = authentication.getName();
        UserInfoResponse userInfo = authService.getCurrentUserInfo(username);
        
        ApiResponse<UserInfoResponse> response = ApiResponse.<UserInfoResponse>builder()
                .success(true)
                .message("Información de usuario obtenida")
                .data(userInfo)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/cambiar-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestParam String passwordActual,
            @RequestParam String passwordNueva,
            Authentication authentication) {
        
        log.info("API: Change password request");
        
        String username = authentication.getName();
        authService.cambiarPassword(username, passwordActual, passwordNueva);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Contraseña cambiada exitosamente")
                .build();
        
        return ResponseEntity.ok(response);
    }
}
