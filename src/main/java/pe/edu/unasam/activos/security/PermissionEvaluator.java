package pe.edu.unasam.activos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import pe.edu.unasam.activos.modules.sistema.domain.Usuario;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.sistema.service.PermisoService;

import java.util.Optional;

@Component("permissionEvaluator")
@RequiredArgsConstructor
public class PermissionEvaluator {

    private final PermisoService permisoService;
    private final UsuarioRepository usuarioRepository;

    public boolean hasPermission(Authentication authentication, String codigoPermiso, String tipoAcceso) {
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

        boolean esAdminGeneral = userDetails.getAuthorities().stream().anyMatch(
                auth -> auth.getAuthority()
                        .equals("ADMIN_GENERAL"));

        if (esAdminGeneral) {
            return true;
        }

        return permisoService.usuarioTienePermiso(
                usuario.getIdUsuario(),
                codigoPermiso,
                tipoAcceso);
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

    @SuppressWarnings("unlikely-arg-type")
    public boolean isOwnerOrAdmin(Authentication authentication, Long usuarioId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        boolean esAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN_GENERAL"));

        if (esAdmin) {
            return true;
        }
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(username);
        return usuarioOpt.isPresent() && usuarioOpt.get().getIdUsuario().equals(usuarioId);
    }
}
