package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.common.enums.EstadoPersona;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String>, JpaSpecificationExecutor<Persona> {

        List<Persona> findByEstadoPersona(EstadoPersona estado);

        List<Persona> findByApellidosContainingIgnoreCaseOrNombresContainingIgnoreCase(
                        String apellidos, String nombres);

        /**
         * Busca personas que NO tienen usuario asignado
         */
        @Query("SELECT p FROM Persona p WHERE NOT EXISTS (SELECT u FROM Usuario u WHERE u.persona.dni = p.dni) " +
                        "AND (:query IS NULL OR :query = '' OR " +
                        "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :query, '%')) " +
                        "OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :query, '%')) " +
                        "OR p.dni LIKE CONCAT('%', :query, '%'))")
        Page<Persona> findPersonasSinUsuarioPaginado(@Param("query") String query, Pageable pageable);

        /**
         * Busca personas que NO están asignadas como responsables
         * NOTA: Descomenta y ajusta según tu entidad Responsable
         */
        @Query("SELECT p FROM Persona p WHERE NOT EXISTS (SELECT r FROM Responsable r WHERE r.persona.dni = p.dni) " +
                        "AND (:query IS NULL OR :query = '' OR " +
                        "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :query, '%')) " +
                        "OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :query, '%')) " +
                        "OR p.dni LIKE CONCAT('%', :query, '%'))")
        Page<Persona> findPersonasSinResponsablePaginado(@Param("query") String query, Pageable pageable);

        /**
         * Busca personas que NO están asignadas como técnicos
         * NOTA: Descomenta y ajusta según tu entidad Tecnico
         */
        @Query("SELECT p FROM Persona p WHERE NOT EXISTS (SELECT t FROM Tecnico t WHERE t.persona.dni = p.dni) " +
                        "AND (:query IS NULL OR :query = '' OR " +
                        "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :query, '%')) " +
                        "OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :query, '%')) " +
                        "OR p.dni LIKE CONCAT('%', :query, '%'))")
        Page<Persona> findPersonasSinTecnicoPaginado(@Param("query") String query, Pageable pageable);

        @Query("SELECT p FROM Persona p WHERE CONCAT(p.apellidos, ' ', p.nombres) LIKE %:nombreCompleto%")
        List<Persona> buscarPorNombreCompleto(@Param("nombreCompleto") String nombreCompleto);

        boolean existsByEmail(String email);

        List<Persona> findByDniNotIn(List<String> dnis);
}
