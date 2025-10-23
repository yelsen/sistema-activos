package pe.edu.unasam.activos.modules.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.enums.EstadoSesion;
import pe.edu.unasam.activos.modules.personas.domain.Usuario;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;
import pe.edu.unasam.activos.modules.sistema.domain.SesionUsuario;
import pe.edu.unasam.activos.modules.sistema.repository.SesionUsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SesionService {
    
    private final SesionUsuarioRepository sesionUsuarioRepository;
    private final UsuarioRepository usuarioRepository; // Inyectar UsuarioRepository
    
    @Transactional
    public void crearSesion(Usuario usuario, String token, String userAgent) {
        log.info("Creando sesión para usuario: {}", usuario.getUsuario());

        // Tu "seguro": si no hay userAgent, no se puede crear la sesión.
        if (!StringUtils.hasText(userAgent)) {
            throw new IllegalArgumentException("No se puede crear una sesión sin un User-Agent válido.");
        }
        
        // Cerrar sesiones activas anteriores del mismo usuario
        List<SesionUsuario> sesionesActivas = sesionUsuarioRepository
                .findByUsuario_IdUsuarioAndEstadoSesion(usuario.getIdUsuario(), EstadoSesion.ACTIVA);
        
        sesionesActivas.forEach(sesion -> {
            sesion.setEstadoSesion(EstadoSesion.CERRADA);
            sesionUsuarioRepository.save(sesion);
        });

        // Actualizar último acceso del usuario (ahora centralizado aquí)
        usuario.setUltimoAcceso(java.time.LocalTime.now());
        usuarioRepository.save(usuario); // Guardar el usuario actualizado
        
        // Crear nueva sesión
        SesionUsuario nuevaSesion = SesionUsuario.builder()
                .usuario(usuario)
                // Para sesiones web, el token es el ID de sesión. Para API, es el JWT.
                // La longitud de la columna debe ser suficiente para ambos.
                .tokenSesion(token) 
                .userAgent(userAgent) // Ya hemos validado que no es nulo/vacío
                .fechaInicio(LocalDateTime.now())
                .fechaUltimoAcceso(LocalDateTime.now()) 
                .fechaExpiracion(LocalDateTime.now().plusDays(1))
                .estadoSesion(EstadoSesion.ACTIVA)
                .build();
        
        sesionUsuarioRepository.save(nuevaSesion);
        log.info("Sesión creada exitosamente");
    }
    
    @Transactional
    public void cerrarSesion(String token) {
        log.info("Cerrando sesión");
        
        sesionUsuarioRepository.findByTokenSesion(token).ifPresent(sesion -> {
            sesion.setEstadoSesion(EstadoSesion.CERRADA);
            sesionUsuarioRepository.save(sesion);
            log.info("Sesión cerrada para usuario ID: {}", sesion.getUsuario().getIdUsuario());
        });
    }
    
    @Transactional
    public void actualizarUltimoAcceso(String token) {
        sesionUsuarioRepository.findByTokenSesion(token).ifPresent(sesion -> {
            sesion.setFechaUltimoAcceso(LocalDateTime.now());
            sesionUsuarioRepository.save(sesion);
        });
    }
    
    @Transactional
    public void expirarSesionesVencidas() {
        log.info("Expirando sesiones vencidas");
        
        List<SesionUsuario> sesionesActivas = sesionUsuarioRepository
                .findByEstadoSesion(EstadoSesion.ACTIVA);
        
        sesionesActivas.forEach(sesion -> {
            if (sesion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
                sesion.setEstadoSesion(EstadoSesion.EXPIRADA);
                sesionUsuarioRepository.save(sesion);
            }
        });
    }
    
    public List<SesionUsuario> getSesionesActivasPorUsuario(Integer idUsuario) {
        return sesionUsuarioRepository
                .findByUsuario_IdUsuarioAndEstadoSesion(idUsuario, EstadoSesion.ACTIVA);
    }
}