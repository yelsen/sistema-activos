package pe.edu.unasam.activos.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import pe.edu.unasam.activos.modules.auth.service.SesionService;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor // Añadir de nuevo para inyección por constructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository; // Inyectar UsuarioRepository
    private final SesionService sesionService; // Inyectar SesionService

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByUsuarioWithRelations(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado."));
        sesionService.crearSesion(usuario, request.getSession().getId(), request.getHeader("User-Agent"));
        setDefaultTargetUrl("/dashboard");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}