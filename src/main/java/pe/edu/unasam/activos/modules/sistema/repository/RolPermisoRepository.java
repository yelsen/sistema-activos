package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermisoId;

import java.util.List;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {

    List<RolPermiso> findByRol_IdRol(Integer idRol);

    @Query("SELECT rp FROM RolPermiso rp JOIN FETCH rp.permiso p JOIN FETCH p.accion JOIN FETCH p.moduloSistema WHERE rp.rol.idRol = :idRol")
    List<RolPermiso> findByRolIdWithPermisos(@Param("idRol") Integer idRol);

    /**
     * CAMBIO CRÍTICO: Agregamos @Modifying y @Transactional para operaciones de eliminación
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM RolPermiso rp WHERE rp.rol.idRol = :idRol")
    void deleteByRol_IdRol(@Param("idRol") Integer idRol);

    @Query("SELECT COUNT(rp) FROM RolPermiso rp WHERE rp.rol.idRol = :idRol")
    long countByRol(@Param("idRol") Integer idRol);

    @Query("SELECT rp.rol.idRol, COUNT(rp.permiso.idPermiso) FROM RolPermiso rp WHERE rp.rol.idRol IN :rolIds AND rp.permitido = true GROUP BY rp.rol.idRol")
    List<Object[]> countPermissionsByRolIds(@Param("rolIds") List<Integer> rolIds);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO rol_permisos (fk_idrol, fk_idpermiso, permitido) VALUES (:idRol, :idPermiso, :permitido)", nativeQuery = true)
    void insertRolPermiso(@Param("idRol") Integer idRol, @Param("idPermiso") Integer idPermiso, @Param("permitido") boolean permitido);
}
