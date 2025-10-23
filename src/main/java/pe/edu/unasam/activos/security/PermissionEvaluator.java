package pe.edu.unasam.activos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import pe.edu.unasam.activos.modules.personas.domain.Usuario;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.sistema.service.PermisoService;

import java.util.Optional;

@Component("permissionEvaluator")
@RequiredArgsConstructor
public class PermissionEvaluator {

    private final PermisoService permisoService;
    private final UsuarioRepository usuarioRepository;

    public boolean hasPermission(Authentication authentication, String codigoPermiso) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(username);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();

        // Si el rol del usuario tiene nivel de acceso 1 (el más alto), tiene todos los permisos.
        if (usuario.getRol() != null && usuario.getRol().getNivelAcceso() != null && usuario.getRol().getNivelAcceso() == 1) {
            return true;
        }

        return permisoService.usuarioTienePermiso(
                usuario.getIdUsuario(),
                codigoPermiso);
    }

    public boolean hasAnyRole(Authentication authentication, String... roles) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        for (String role : roles) {
            if (userDetails.getAuthorities().stream().anyMatch(
                    auth -> auth.getAuthority()
                            .equals(role))) {
                return true;
            }
        }

        return false;
    }

    public boolean isOwnerOrAdmin(Authentication authentication, Long usuarioId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return false;
        }

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(username);
        return usuarioOpt.map(usuario -> {
            // Es admin? (nivel 1)
            boolean isAdmin = usuario.getRol() != null && usuario.getRol().getNivelAcceso() != null && usuario.getRol().getNivelAcceso() == 1;
            // Es el dueño del recurso? (Comparando los valores numéricos)
            boolean isOwner = usuario.getIdUsuario().longValue() == usuarioId;
            return isAdmin || isOwner;
        }).orElse(false);
    }
}
