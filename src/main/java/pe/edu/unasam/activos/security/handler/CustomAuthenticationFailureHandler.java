package pe.edu.unasam.activos.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import pe.edu.unasam.activos.modules.personas.domain.Usuario;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;
import pe.edu.unasam.activos.modules.sistema.repository.ConfiguracionSistemaRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UsuarioRepository usuarioRepository;
    private final ConfiguracionSistemaRepository configuracionRepo;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        // Fallback: algunos flujos pueden no enviar el parámetro; usamos el último
        // username en sesión.
        if (username == null || username.isBlank()) {
            var session = request.getSession(false);
            Object lastUsername = session != null ? session.getAttribute("SPRING_SECURITY_LAST_USERNAME") : null;
            if (lastUsername instanceof String s && !s.isBlank()) {
                username = s.trim();
            }
        } else {
            username = username.trim();
        }

        if (exception instanceof LockedException) {
            String failureUrl = "/login?locked=true";

            if (username != null && !username.isBlank()) {
                long minutesLeft = usuarioRepository.findByUsuario(username)
                        .map(Usuario::getBloqueadoHasta)
                        .filter(dt -> dt.isAfter(LocalDateTime.now()))
                        .map(dt -> ChronoUnit.MINUTES.between(LocalDateTime.now(), dt))
                        .orElse(getBlockMinutes());
                failureUrl += "&lockedMinutesLeft=" + minutesLeft;
            }

            setDefaultFailureUrl(failureUrl);
        } else {
            int maxAttempts = getMaxAttempts();
            long blockMinutes = getBlockMinutes();
            Integer remaining = null;
            boolean sessionBlockTriggered = false;
            long sessionLockMinutesLeft = 0L;

            // Unificar contador: cada intento fallido incrementa un único contador por
            // sesión,
            // sin importar si el usuario existe o la contraseña falló.
            var session = request.getSession(true);
            Integer sessionAttempts = (Integer) session.getAttribute("SESSION_LOGIN_ATTEMPTS");
            int attempts = sessionAttempts == null ? 1 : sessionAttempts + 1;
            session.setAttribute("SESSION_LOGIN_ATTEMPTS", attempts);

            if (attempts >= maxAttempts) {
                var blockUntil = LocalDateTime.now().plusMinutes(blockMinutes);
                session.setAttribute("SESSION_BLOCK_UNTIL", blockUntil);
                sessionLockMinutesLeft = blockMinutes;
                sessionBlockTriggered = true;
                remaining = 0;
            } else {
                remaining = Math.max(0, maxAttempts - attempts);
            }

            String failureUrl = sessionBlockTriggered
                    ? "/login?locked=true&lockedMinutesLeft=" + sessionLockMinutesLeft
                    : "/login?error=true" +
                            (remaining != null ? "&remaining=" + remaining + "&max=" + maxAttempts : "") +
                            "&lockMinutes=" + blockMinutes;
            setDefaultFailureUrl(failureUrl);
        }

        super.onAuthenticationFailure(request, response, exception);
    }

    private int getMaxAttempts() {
        return configuracionRepo.findByClaveConfig("seguridad.intentos_fallidos")
                .map(ConfiguracionSistema::getValorConfig)
                .map(Integer::parseInt)
                .orElse(5);
    }

    private long getBlockMinutes() {
        return configuracionRepo.findByClaveConfig("seguridad.tiempo_bloqueo_minutos")
                .map(ConfiguracionSistema::getValorConfig)
                .map(Long::parseLong)
                .orElse(15L);
    }
}
