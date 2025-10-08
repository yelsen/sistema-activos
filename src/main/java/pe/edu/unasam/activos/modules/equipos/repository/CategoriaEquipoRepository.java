package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.CategoriaEquipo;

import java.util.Optional;

@Repository
public interface CategoriaEquipoRepository extends JpaRepository<CategoriaEquipo, Integer> {
    
    Optional<CategoriaEquipo> findByCategoriaEquipo(String categoriaEquipo);
}
