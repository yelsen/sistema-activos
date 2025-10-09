package pe.edu.unasam.activos.modules.sistema.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pe.edu.unasam.activos.common.enums.EstadoSesion;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.sistema.domain.SesionUsuario;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;
import pe.edu.unasam.activos.modules.sistema.dto.SesionUsuarioResponse;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.sistema.repository.SesionUsuarioRepository;
import jakarta.persistence.criteria.Expression;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SesionUsuarioService {

    private final SesionUsuarioRepository sesionUsuarioRepository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Page<SesionUsuarioResponse> getSesiones(String query, EstadoSesion estado, Pageable pageable) {
        log.info("Buscando sesiones con query: '{}', estado: '{}'", query, estado);

        Specification<SesionUsuario> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(query)) {
                Join<SesionUsuario, Usuario> usuarioJoin = root.join("usuario");
                Join<Usuario, Persona> personaJoin = usuarioJoin.join("persona");
                String pattern = "%" + query.toLowerCase() + "%";

                // Usamos CONCAT_WS para una concatenación más limpia y eficiente
                Expression<String> nombreCompleto = criteriaBuilder.function("CONCAT_WS", String.class,
                        criteriaBuilder.literal(" "),
                        personaJoin.get("nombres"),
                        personaJoin.get("apellidos"));

                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(nombreCompleto), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(usuarioJoin.get("usuario")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("userAgent")), pattern)));
            }
            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estadoSesion"), estado));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<SesionUsuario> sesionesPage = sesionUsuarioRepository.findAll(spec, pageable);
        return sesionesPage.map(this::convertToDto);
    }

    @Transactional
    public void closeSession(Integer idSesion) {
        log.info("Cerrando sesión con ID: {}", idSesion);
        SesionUsuario sesion = sesionUsuarioRepository.findById(idSesion)
                .orElseThrow(() -> new NotFoundException("Sesión no encontrada con ID: " + idSesion));

        if (sesion.getEstadoSesion() == EstadoSesion.ACTIVA) {
            sesion.setEstadoSesion(EstadoSesion.CERRADA);
            log.info("Sesión ID: {} cerrada exitosamente por un administrador.", idSesion);
        } else {
            log.warn("Intento de cerrar una sesión que no está activa. ID: {}", idSesion);
        }
    }

    @Transactional
    public void closeMultipleSessions(List<Integer> ids) {
        log.info("Cerrando masivamente {} sesiones", ids.size());
        List<SesionUsuario> sesiones = sesionUsuarioRepository.findAllById(ids);

        sesiones.forEach(sesion -> {
            if (sesion.getEstadoSesion() == EstadoSesion.ACTIVA) {
                sesion.setEstadoSesion(EstadoSesion.CERRADA);
            }
        });
        
        log.info("Cierre masivo completado.");
    }

    private SesionUsuarioResponse convertToDto(SesionUsuario sesion) {
        String nombreCompleto = (sesion.getUsuario() != null && sesion.getUsuario().getPersona() != null)
                ? sesion.getUsuario().getPersona().getNombres() + " " + sesion.getUsuario().getPersona().getApellidos()
                : "Usuario Desconocido";

        return SesionUsuarioResponse.builder()
                .idSesionUsuario(sesion.getIdSesionUsuario())
                .nombreCompleto(nombreCompleto)
                .userAgent(sesion.getUserAgent())
                .fechaInicio(sesion.getFechaInicio() != null ? sesion.getFechaInicio().format(FORMATTER) : "-")
                .fechaUltimoAcceso(
                        sesion.getFechaUltimoAcceso() != null ? sesion.getFechaUltimoAcceso().format(FORMATTER) : "-")
                .estadoSesion(sesion.getEstadoSesion())
                .build();
    }
}
