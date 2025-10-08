package pe.edu.unasam.activos.modules.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.BadRequestException;
import pe.edu.unasam.activos.common.exception.UnauthorizedException;
import pe.edu.unasam.activos.modules.auth.dto.*;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;
import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;
import pe.edu.unasam.activos.modules.sistema.repository.ConfiguracionSistemaRepository;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;
import pe.edu.unasam.activos.security.JwtService;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;

import java.util.Collections;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final SesionService sesionService;
    private final PasswordEncoder passwordEncoder;
    private final ConfiguracionSistemaRepository configuracionSistemaRepository;

    @Transactional
    public TokenResponse login(LoginRequest request) {

        // Buscar usuario
        Usuario usuario = usuarioRepository.findByUsuarioWithRelations(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        // Verificar si está bloqueado
        if (usuario.getBloqueadoHasta() != null &&
                usuario.getBloqueadoHasta().isAfter(LocalDateTime.now())) {
            throw new UnauthorizedException("Usuario bloqueado temporalmente. Intente más tarde.");
        }

        // Verificar estado del usuario
        if (usuario.getEstadoUsuarios() != EstadoUsuario.ACTIVO) {
            throw new UnauthorizedException("Usuario inactivo. Contacte al administrador.");
        }

        try {
            // Autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
            return handleSuccessfulAuthentication(usuario, authentication, null);

        } catch (BadCredentialsException e) {
            registrarIntentoFallido(usuario);
            throw new UnauthorizedException("Credenciales inválidas");
        }
    }

    @Transactional
    public TokenResponse handleSuccessfulAuthentication(Usuario usuario, Authentication authentication,
            String userAgent) {
        // Generar tokens
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", usuario.getIdUsuario());
        extraClaims.put("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        String token = jwtService.generateToken(extraClaims,
                org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getUsuario())
                        .password(usuario.getContrasena())
                        .authorities(authentication.getAuthorities())
                        .build());

        // Crear sesión
        sesionService.crearSesion(usuario, token, userAgent);

        // Preparar respuesta
        UserInfoResponse userInfo = buildUserInfo(usuario, authentication);

        return TokenResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400000L) // 24 horas
                .userInfo(userInfo)
                .build();
    }

    @Transactional
    public void logout(String token) {
        log.info("Cerrando sesión");
        sesionService.cerrarSesion(token);
    }

    public UserInfoResponse getCurrentUserInfo(String username) {
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado"));
        // Para obtener la info completa, necesitamos la autenticación, pero aquí no la tenemos.
        // Devolvemos info básica. Para info completa, el front-end debería usar el endpoint que sí tiene el contexto de seguridad.
        return buildUserInfo(usuario, Collections.emptyList());
    }

    @Transactional
    public void cambiarPassword(String username, String passwordActual, String passwordNueva) {
        log.info("Cambiando contraseña para usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado"));

        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordActual, usuario.getContrasena())) {
            throw new BadRequestException("La contraseña actual es incorrecta");
        }

        // Validar nueva contraseña
        if (passwordNueva.length() < 8) {
            throw new BadRequestException("La contraseña debe tener al menos 8 caracteres");
        }

        // Actualizar contraseña
        usuario.setContrasena(passwordEncoder.encode(passwordNueva));
        usuario.setDebeCambiarPassword(false);
        usuarioRepository.save(usuario);
    }

    // ==================== MÉTODOS PRIVADOS ====================

    private void registrarIntentoFallido(Usuario usuario) {
        int maxIntentos = Integer.parseInt(configuracionSistemaRepository
                .findByClaveConfig("seguridad.intentos_fallidos")
                .map(ConfiguracionSistema::getValorConfig).orElse("5"));
        int minutosBloqueo = Integer.parseInt(configuracionSistemaRepository
                .findByClaveConfig("seguridad.tiempo_bloqueo_minutos")
                .map(ConfiguracionSistema::getValorConfig).orElse("15"));

        int intentos = usuario.getIntentosFallidos() != null ? usuario.getIntentosFallidos() : 0;
        intentos++;
        usuario.setIntentosFallidos(intentos);

        if (intentos >= maxIntentos) {
            usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(minutosBloqueo));
            log.warn("Usuario {} bloqueado por {} minutos debido a múltiples intentos fallidos",
                    usuario.getUsuario(), minutosBloqueo);
        }

        usuarioRepository.save(usuario);
    }

    private UserInfoResponse buildUserInfo(Usuario usuario, List<RolPermiso> rolPermisos) {
        List<String> roles = new ArrayList<>();
        if (usuario.getRol() != null) {
            roles.add("ROLE_" + usuario.getRol().getNombreRol());
        }

        // Extraer los códigos de permiso de la lista de RolPermiso
        List<String> permisos = rolPermisos.stream()
                .filter(rp -> rp.isPermitido() && rp.getPermiso() != null)
                .map(rp -> rp.getPermiso().getCodigoPermiso())
                .distinct() // Asegurarse de que no haya duplicados
                .collect(Collectors.toList());

        String nombreCompleto = usuario.getPersona() != null
                ? usuario.getPersona().getNombres() + " " + usuario.getPersona().getApellidos()
                : usuario.getUsuario();

        String email = usuario.getPersona() != null ? usuario.getPersona().getEmail() : null;
        String documento = usuario.getPersona() != null ? usuario.getPersona().getDocumento() : null;

        return UserInfoResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .usuario(usuario.getUsuario())
                .nombreCompleto(nombreCompleto)
                .email(email)
                .documento(documento)
                .roles(roles)
                .permisos(permisos)
                .debeCambiarPassword(usuario.getDebeCambiarPassword())
                .build();
    }

    // Sobrecarga del método para usarlo desde el SuccessHandler
    private UserInfoResponse buildUserInfo(Usuario usuario, Authentication authentication) {
        List<String> roles = new ArrayList<>();
        if (usuario.getRol() != null) {
            roles.add("ROLE_" + usuario.getRol().getNombreRol());
        }

        List<String> permisos = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_"))
                .collect(Collectors.toList());

        String nombreCompleto = usuario.getPersona().getNombres() + " " + usuario.getPersona().getApellidos();

        return UserInfoResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .usuario(usuario.getUsuario())
                .nombreCompleto(nombreCompleto)
                .email(usuario.getPersona().getEmail())
                .documento(usuario.getPersona().getDocumento())
                .roles(roles)
                .permisos(permisos)
                .debeCambiarPassword(usuario.getDebeCambiarPassword())
                .build();
    }
}
