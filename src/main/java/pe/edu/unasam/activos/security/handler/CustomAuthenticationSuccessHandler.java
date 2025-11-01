package pe.edu.unasam.activos.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import pe.edu.unasam.activos.common.exception.UnauthorizedException;
import pe.edu.unasam.activos.modules.auth.dto.UserInfoResponse;
import pe.edu.unasam.activos.modules.auth.service.SesionService;
import pe.edu.unasam.activos.modules.personas.domain.Usuario;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UsuarioRepository usuarioRepository;
    private final SesionService sesionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // Asegurar sesión HTTP
        var session = request.getSession();

        // Construir y registrar sesión de usuario en BD
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsuarioWithRelations(username)
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado"));

        String tokenSesion = session.getId();
        String userAgent = request.getHeader("User-Agent");
        sesionService.crearSesion(usuario, tokenSesion, userAgent);

        // Guardar token e info de usuario en la sesión
        session.setAttribute("token", tokenSesion);
        session.setAttribute("user", buildUserInfo(usuario, authentication));

        // Limpiar contadores de intentos/lock de la sesión
        session.removeAttribute("SESSION_LOGIN_ATTEMPTS");
        session.removeAttribute("SESSION_BLOCK_UNTIL");

        // Flash message para mostrar toast de "Sesión iniciada"
        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
        FlashMap flashMap = new FlashMap();
        flashMap.put("success", "Sesión iniciada exitosamente.");
        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        setDefaultTargetUrl("/home");
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private UserInfoResponse buildUserInfo(Usuario usuario, Authentication authentication) {
        // Roles: desde la entidad Usuario (consistente con AuthService)
        List<String> roles = new ArrayList<>();
        if (usuario.getRol() != null) {
            roles.add("ROLE_" + usuario.getRol().getNombreRol());
        }

        // Permisos: desde authorities no ROLE_
        List<String> permisos = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_"))
                .collect(Collectors.toList());

        String nombreCompleto = usuario.getPersona() != null
                ? usuario.getPersona().getNombres() + " " + usuario.getPersona().getApellidos()
                : usuario.getUsuario();
        String email = usuario.getPersona() != null ? usuario.getPersona().getEmail() : null;
        String documento = usuario.getPersona() != null ? usuario.getPersona().getDni() : null;

        return UserInfoResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .usuario(usuario.getUsuario())
                .nombreCompleto(nombreCompleto)
                .email(email)
                .documento(documento)
                .roles(roles)
                .permisos(permisos)
                .build();
    }
}
