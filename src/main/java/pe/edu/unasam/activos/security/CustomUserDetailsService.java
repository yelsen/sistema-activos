package pe.edu.unasam.activos.security;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.sistema.repository.RolPermisoRepository;
import pe.edu.unasam.activos.modules.sistema.repository.PermisoRepository;
import pe.edu.unasam.activos.security.service.LoginAttemptService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final LoginAttemptService loginAttemptService;

    public CustomUserDetailsService(
            UsuarioRepository usuarioRepository,
            RolPermisoRepository rolPermisoRepository,
            PermisoRepository permisoRepository,
            LoginAttemptService loginAttemptService) {
        this.usuarioRepository = usuarioRepository;
        this.rolPermisoRepository = rolPermisoRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Usuario bloqueado debido a múltiples intentos fallidos.");
        }

        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getUsuario())
                .password(usuario.getContrasena())
                .authorities(getAuthorities(usuario))
                .accountExpired(false)
                .accountLocked(isAccountLocked(usuario))
                .credentialsExpired(usuario.getDebeCambiarPassword() != null && usuario.getDebeCambiarPassword())
                .disabled(usuario.getEstadoUsuarios() == pe.edu.unasam.activos.common.enums.EstadoUsuario.INACTIVO)
                .build();
    }

    private boolean isAccountLocked(Usuario usuario) {
        return (usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(LocalDateTime.now()))
                || usuario.getEstadoUsuarios() == pe.edu.unasam.activos.common.enums.EstadoUsuario.BLOQUEADO;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        Rol rol = usuario.getRol();
        if (rol != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol()));
            rolPermisoRepository.findByRolIdWithPermisos(rol.getIdRol())
                    .stream()
                    .filter(rolPermiso -> rolPermiso.isPermitido() && rolPermiso.getPermiso() != null)
                    .forEach(rolPermiso -> {
                        authorities.add(new SimpleGrantedAuthority(rolPermiso.getPermiso().getCodigoPermiso()));
                    });
        }

        return authorities;
    }
}
