package pe.edu.unasam.activos.modules.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.auth.domain.PasswordResetCode;
import pe.edu.unasam.activos.modules.auth.repository.PasswordResetCodeRepository;
import pe.edu.unasam.activos.modules.personas.domain.Usuario;
import pe.edu.unasam.activos.modules.personas.repository.UsuarioRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final PasswordResetCodeRepository codeRepository;
    private final UsuarioRepository usuarioRepository;

    private static final int CODE_LENGTH = 6; // 6 dígitos
    private static final long EXPIRATION_MINUTES = 10; // 10 minutos

    @Transactional
    public void generateAndSendCode(String username) {
        Usuario usuario = usuarioRepository.findByUsuarioWithRelations(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Limpiar códigos vencidos anteriores
        codeRepository.deleteByUsernameAndExpiresAtBefore(username, LocalDateTime.now());

        // Generar nuevo código
        String code = generateNumericCode(CODE_LENGTH);
        PasswordResetCode entity = PasswordResetCode.builder()
                .username(username)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES))
                .used(false)
                .intentos(0)
                .createdAt(LocalDateTime.now())
                .build();
        codeRepository.save(entity);

        // Enviar por WhatsApp (placeholder)
        String telefono = usuario.getPersona() != null ? usuario.getPersona().getTelefono() : null;
        sendWhatsAppCode(telefono, code);
    }

    @Transactional
    public boolean verifyAndConsumeCode(String username, String code) {
        PasswordResetCode entity = codeRepository.findTopByUsernameAndUsedFalseOrderByCreatedAtDesc(username)
                .orElseThrow(() -> new RuntimeException("No hay solicitud de recuperación activa"));

        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            entity.setUsed(true);
            codeRepository.save(entity);
            throw new RuntimeException("El código ha expirado");
        }

        // Incrementar intentos y validar
        entity.setIntentos(entity.getIntentos() + 1);
        boolean ok = entity.getCode().equals(code);
        if (ok) {
            entity.setUsed(true);
        }
        codeRepository.save(entity);
        return ok;
    }

    private String generateNumericCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private void sendWhatsAppCode(String telefono, String code) {
        if (telefono == null || telefono.isBlank()) {
            return;
        }
    }
}
