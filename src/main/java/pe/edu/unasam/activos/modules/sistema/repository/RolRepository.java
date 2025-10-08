package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.sistema.domain.Rol;
import pe.edu.unasam.activos.common.enums.EstadoRol;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>, JpaSpecificationExecutor<Rol> {

    Optional<Rol> findByNombreRol(String nombreRol);

    List<Rol> findByEstadoRol(EstadoRol estadoRol);

    boolean existsByNombreRol(String nombreRol);

    List<Rol> findByNivelAccesoLessThanEqual(Integer nivelAcceso);
}
