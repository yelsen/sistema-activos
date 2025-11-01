package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unasam.activos.modules.sistema.domain.Accion;

import java.util.Optional;

public interface AccionRepository extends JpaRepository<Accion, Integer> {

    Optional<Accion> findByCodigoAccion(String codigo);

    Optional<Accion> findByNombreAccion(String nombreAccion);

    boolean existsByCodigoAccion(String codigoAccion);

    boolean existsByNombreAccion(String nombreAccion);

    Page<Accion> findByNombreAccionContainingIgnoreCase(String nombreAccion, Pageable pageable);
}
