package pe.edu.unasam.activos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.personas.domain.Usuario;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.sistema.repository.RolPermisoRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RolPermisoRepository rolPermisoRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // La lógica de bloqueo ya se maneja en AuthService antes de la autenticación.
        // Aquí solo cargamos los datos del usuario si las credenciales son correctas.
        Usuario usuario = usuarioRepository.findByUsuarioWithRelations(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getUsuario())
                .password(usuario.getContrasena())
                .authorities(getAuthorities(usuario))
                .accountExpired(false)
                .accountLocked(isAccountLocked(usuario))
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
                    .map(rolPermiso -> new SimpleGrantedAuthority(rolPermiso.getPermiso().getCodigoPermiso()))
                    .forEach(authorities::add);
        }

        return authorities;
    }
}
