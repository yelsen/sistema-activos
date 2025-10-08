package pe.edu.unasam.activos.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;
import pe.edu.unasam.activos.modules.sistema.repository.ConfiguracionSistemaRepository;
import pe.edu.unasam.activos.modules.sistema.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final int MAX_ATTEMPTS;
    private final long BLOCK_DURATION_MINUTES;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public LoginAttemptServiceImpl(ConfiguracionSistemaRepository configuracionRepo,
            UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.MAX_ATTEMPTS = configuracionRepo.findByClaveConfig("auth.login.max_attempts")
                .map(ConfiguracionSistema::getValorConfig)
                .map(Integer::parseInt)
                .orElse(3);
        this.BLOCK_DURATION_MINUTES = configuracionRepo.findByClaveConfig("seguridad.tiempo_bloqueo_minutos")
                .map(ConfiguracionSistema::getValorConfig)
                .map(Long::parseLong)
                .orElse(15L);
    }

    @Override
    @Transactional
    public void loginSucceeded(String key) {
        usuarioRepository.findByUsuario(key).ifPresent(usuario -> {
            if (usuario.getIntentosFallidos() != null && usuario.getIntentosFallidos() > 0) {
                usuario.setIntentosFallidos(0);
                usuario.setBloqueadoHasta(null);
                usuarioRepository.save(usuario);
            }
        });
    }

    @Override
    @Transactional
    public void loginFailed(String key) {
        usuarioRepository.findByUsuario(key).ifPresent(usuario -> {
            int attempts = usuario.getIntentosFallidos() != null ? usuario.getIntentosFallidos() : 0;
            attempts++;
            usuario.setIntentosFallidos(attempts);

            if (attempts >= MAX_ATTEMPTS) {
                usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));
            }
            usuarioRepository.save(usuario);
        });
    }

    @Override
    public boolean isBlocked(String key) {
        return usuarioRepository.findByUsuario(key)
                .map(usuario -> usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta()
                        .isAfter(LocalDateTime.now()))
                .orElse(false);
    }
}
