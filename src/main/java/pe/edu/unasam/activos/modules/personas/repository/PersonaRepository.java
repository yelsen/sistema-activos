package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.common.enums.EstadoPersona;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String>, JpaSpecificationExecutor<Persona> {

    List<Persona> findByEstadoPersona(EstadoPersona estado);

    List<Persona> findByApellidosContainingIgnoreCaseOrNombresContainingIgnoreCase(
            String apellidos, String nombres);

    @Query(value = "SELECT p FROM Persona p WHERE NOT EXISTS (SELECT u FROM Usuario u WHERE u.persona.numeroDocumento = p.numeroDocumento) "
            +
            "AND (:query IS NULL OR :query = '' OR " +
            "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR p.numeroDocumento LIKE CONCAT('%', :query, '%'))")
    Page<Persona> findPersonasSinUsuarioPaginado(String query, Pageable pageable);

    @Query("SELECT p FROM Persona p WHERE CONCAT(p.apellidos, ' ', p.nombres) LIKE %:nombreCompleto%")
    List<Persona> buscarPorNombreCompleto(String nombreCompleto);

    boolean existsByEmail(String email);

    List<Persona> findByNumeroDocumentoNotIn(List<String> numeroDocumentos);
}
