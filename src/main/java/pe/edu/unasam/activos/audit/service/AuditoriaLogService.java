package pe.edu.unasam.activos.audit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pe.edu.unasam.activos.audit.domain.AuditoriaLog;
import pe.edu.unasam.activos.audit.repository.AuditoriaLogRepository;
import pe.edu.unasam.activos.common.enums.AccionAuditoria;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuditoriaLogService {

    private final AuditoriaLogRepository auditoriaLogRepository;

    public Page<AuditoriaLog> getLogs(String query, String modulo, AccionAuditoria accion, LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        Specification<AuditoriaLog> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<AuditoriaLog, Usuario> usuarioJoin = root.join("usuario");

            if (StringUtils.hasText(query)) {
                Join<Usuario, Persona> personaJoin = usuarioJoin.join("persona");
                String pattern = "%" + query.toLowerCase() + "%";

                Expression<String> nombreCompleto = criteriaBuilder.function("CONCAT_WS", String.class,
                        criteriaBuilder.literal(" "),
                        personaJoin.get("nombres"),
                        personaJoin.get("apellidos"));

                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(nombreCompleto), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(usuarioJoin.get("usuario")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("ipAddress")), pattern)
                ));
            }

            if (StringUtils.hasText(modulo)) {
                predicates.add(criteriaBuilder.equal(root.get("tablaAfectada"), modulo));
            }

            if (accion != null) {
                predicates.add(criteriaBuilder.equal(root.get("accion"), accion));
            }

            if (fechaInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fechaHora"), fechaInicio.atStartOfDay()));
            }

            if (fechaFin != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fechaHora"), fechaFin.atTime(LocalTime.MAX)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return auditoriaLogRepository.findAll(spec, pageable);
    }

    public List<String> getDistinctModulos() {
        return auditoriaLogRepository.findDistinctTablaAfectada();
    }
}
