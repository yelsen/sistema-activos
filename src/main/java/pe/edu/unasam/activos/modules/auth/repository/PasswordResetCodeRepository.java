package pe.edu.unasam.activos.modules.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.auth.domain.PasswordResetCode;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Integer> {

    Optional<PasswordResetCode> findTopByUsernameAndUsedFalseOrderByCreatedAtDesc(String username);

    long deleteByUsernameAndExpiresAtBefore(String username, LocalDateTime before);
}
