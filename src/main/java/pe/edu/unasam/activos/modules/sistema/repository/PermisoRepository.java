package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.sistema.domain.Permiso;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

    /* Busca un permiso por su código único */
    Optional<Permiso> findByCodigoPermiso(String codigoPermiso);

    /* Busca permisos por módulo */
    @Query("SELECT p FROM Permiso p WHERE p.moduloSistema.idModuloSistemas = :idModulo")
    List<Permiso> findByModuloSistemaId(@Param("idModulo") Integer idModulo);

    /* Busca permisos por acción */
    @Query("SELECT p FROM Permiso p WHERE p.accion.idAccion = :idAccion")
    List<Permiso> findByAccionId(@Param("idAccion") Integer idAccion);

    /* Busca permisos por módulo y acción */
    @Query("SELECT p FROM Permiso p WHERE p.moduloSistema.idModuloSistemas = :idModulo AND p.accion.idAccion = :idAccion")
    Optional<Permiso> findByModuloAndAccion(@Param("idModulo") Integer idModulo, @Param("idAccion") Integer idAccion);

    /* Busca todos los permisos con sus relaciones cargadas */
    @Query("SELECT p FROM Permiso p JOIN FETCH p.moduloSistema JOIN FETCH p.accion")
    List<Permiso> findAllWithRelations();

    /* Verifica si existe un permiso con un código específico */
    boolean existsByCodigoPermiso(String codigoPermiso);

    /* Busca permisos por nombre del módulo */
    @Query("SELECT p FROM Permiso p WHERE p.moduloSistema.nombreModulo = :nombreModulo")
    List<Permiso> findByNombreModulo(@Param("nombreModulo") String nombreModulo);

    /* Busca permisos por código de acción */
    @Query("SELECT p FROM Permiso p WHERE p.accion.codigoAccion = :codigoAccion")
    List<Permiso> findByCodigoAccion(@Param("codigoAccion") String codigoAccion);

    /* Busca todos los permisos cargando ansiosamente las relaciones de módulo y acción */
    @Query("SELECT p FROM Permiso p JOIN FETCH p.moduloSistema JOIN FETCH p.accion")
    List<Permiso> findAllWithModuloAndAccion();
}
