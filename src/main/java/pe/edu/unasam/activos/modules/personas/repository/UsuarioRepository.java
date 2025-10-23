package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.modules.personas.domain.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

        Optional<Usuario> findByUsuario(String usuario);

        boolean existsByUsuario(String usuario);

        boolean existsByUsuarioAndIdUsuarioNot(String usuario, Integer idUsuario);

        boolean existsByPersona(Persona persona);

        boolean existsByPersona_NumeroDocumento(String numeroDocumento);

        long countByRol(Rol rol);

        /**
         * Búsqueda mejorada con filtros optimizados
         * - Búsqueda insensible a mayúsculas/minúsculas
         * - Búsqueda en múltiples campos
         * - Filtro opcional por estado
         * - Eager loading de relaciones para evitar N+1 queries
         */
        @Query("SELECT DISTINCT u FROM Usuario u " +
                        "LEFT JOIN FETCH u.persona p " +
                        "LEFT JOIN FETCH p.tipoDocumento " +
                        "LEFT JOIN FETCH u.rol " +
                        "WHERE (:query IS NULL OR " +
                        "  LOWER(p.numeroDocumento) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "  LOWER(CONCAT(p.nombres, ' ', p.apellidos)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "  LOWER(u.usuario) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "  LOWER(p.email) LIKE LOWER(CONCAT('%', :query, '%'))) " +
                        "AND (:estado IS NULL OR u.estadoUsuarios = :estado)")
        Page<Usuario> findAllWithFilters(
                        @Param("query") String query,
                        @Param("estado") EstadoUsuario estado,
                        Pageable pageable);

        @Query("SELECT u FROM Usuario u " +
                        "LEFT JOIN FETCH u.persona " +
                        "LEFT JOIN FETCH u.rol " +
                        "WHERE u.usuario = :username")
        Optional<Usuario> findByUsuarioWithRelations(@Param("username") String username);

        @Query("SELECT r.idRol, COUNT(u) FROM Usuario u " +
                        "JOIN u.rol r " +
                        "WHERE r.idRol IN :rolIds " +
                        "GROUP BY r.idRol")
        List<Object[]> countUsersByRolIds(@Param("rolIds") List<Integer> rolIds);

        @Query("SELECT u FROM Usuario u WHERE u.rol.idRol = :rolId")
        List<Usuario> findByRolId(@Param("rolId") Integer rolId);
}
