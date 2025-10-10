package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.sistema.domain.Usuario;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByUsuario(String usuario);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.persona p JOIN FETCH u.rol WHERE u.usuario = :usuario")
    Optional<Usuario> findByUsuarioWithRelations(@Param("usuario") String usuario);

    boolean existsByUsuario(String usuario);

    List<Usuario> findByEstadoUsuarios(EstadoUsuario estadoUsuarios);

    @Query("SELECT u FROM Usuario u WHERE u.persona.documento = :documento")
    Optional<Usuario> findByPersonaDocumento(@Param("documento") String documento);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.persona WHERE u.estadoUsuarios = :estado")
    List<Usuario> findAllByEstadoWithPersona(@Param("estado") EstadoUsuario estado);

    long countByRol(Rol rol);

    @Query("SELECT r.idRol, COUNT(u.idUsuario) FROM Usuario u JOIN u.rol r WHERE r.idRol IN :rolIds GROUP BY r.idRol")
    List<Object[]> countUsersByRolIds(@Param("rolIds") List<Integer> rolIds);

}
