package pe.edu.unasam.activos.security.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.modules.auth.service.AuthService;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final AuthService authService;

    @SuppressWarnings("null")
    @Override
    public void onApplicationEvent( AuthenticationFailureBadCredentialsEvent event) {
        
        String username = (String) event.getAuthentication().getPrincipal();
        authService.handleFailedLoginAttempt(username);
    }
}
