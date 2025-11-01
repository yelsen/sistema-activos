package pe.edu.unasam.activos.modules.ubicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Origen;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface OrigenRepository extends JpaRepository<Origen, Integer> {

    Optional<Origen> findByNombreOrigen(String nombreOrigen);

    boolean existsByNombreOrigenIgnoreCase(String nombreOrigen);

    List<Origen> findByNombreOrigenContainingIgnoreCase(String nombreOrigen);

    Page<Origen> findByNombreOrigenContainingIgnoreCase(String nombreOrigen, Pageable pageable);
}
