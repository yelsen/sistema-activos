package pe.edu.unasam.activos.security.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.modules.auth.service.AuthService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AuthService authService;

    @SuppressWarnings("null")
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        log.info("Login exitoso para el usuario: {}. Reseteando contador de intentos fallidos.", username);
        authService.resetFailedLoginAttempts(username);

        // Registrar la sesión y almacenar datos en HttpSession usando el Success Event
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            try {
                authService.onFormLoginSuccess(request, event.getAuthentication());
            } catch (Exception e) {
                log.error("Error al registrar sesión tras login exitoso", e);
            }
        }
    }
}
