package pe.edu.unasam.activos.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class SessionLoginRateLimitFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Intercepta solo el procesamiento del login por formulario, manejando
        // contextPath
        boolean isLoginPost = "POST".equalsIgnoreCase(request.getMethod());
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath))
                ? uri.substring(contextPath.length())
                : uri;
        isLoginPost = isLoginPost && ("/login".equals(path) || path.endsWith("/login"));

        if (isLoginPost) {
            var session = request.getSession(false);
            if (session != null) {
                Object untilObj = session.getAttribute("SESSION_BLOCK_UNTIL");
                if (untilObj instanceof LocalDateTime blockUntil) {
                    if (LocalDateTime.now().isBefore(blockUntil)) {
                        long minutesLeft = Math.max(0,
                                ChronoUnit.MINUTES.between(LocalDateTime.now(), blockUntil));
                        String failureUrl = (contextPath != null ? contextPath : "") +
                                "/login?locked=true&lockedMinutesLeft=" + minutesLeft;
                        response.sendRedirect(failureUrl);
                        return;
                    } else {
                        // Bloqueo expirado, limpiar rastros
                        session.removeAttribute("SESSION_BLOCK_UNTIL");
                        session.removeAttribute("SESSION_LOGIN_ATTEMPTS");
                    }
                } else if (untilObj != null) {
                    // Valor inesperado: limpiar para evitar estados inválidos
                    session.removeAttribute("SESSION_BLOCK_UNTIL");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
