package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import pe.edu.unasam.activos.modules.personas.domain.Cargo;

import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    Optional<Cargo> findByNombreCargo(String nombreCargo);
    
    boolean existsByNombreCargo(String nombreCargo);

    Page<Cargo> findByNombreCargoContainingIgnoreCase(String nombreCargo, Pageable pageable);
}
