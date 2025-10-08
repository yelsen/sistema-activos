package pe.edu.unasam.activos.modules.ubicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Origen;

import java.util.Optional;

@Repository
public interface OrigenRepository extends JpaRepository<Origen, Integer> {

    Optional<Origen> findByNombreOrigen(String nombreOrigen);
}