package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.unasam.activos.modules.sistema.domain.RolPermiso;
import pe.edu.unasam.activos.modules.sistema.domain.RolPermisoId;

import java.util.List;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {

    List<RolPermiso> findByRol_IdRol(Integer idRol);

    @Query("SELECT rp FROM RolPermiso rp JOIN FETCH rp.permiso p JOIN FETCH p.accion JOIN FETCH p.moduloSistema WHERE rp.rol.idRol = :idRol")
    List<RolPermiso> findByRolIdWithPermisos(@Param("idRol") Integer idRol);

    void deleteByRol_IdRol(Integer idRol);
}
