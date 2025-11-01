package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.CategoriaComponente;

import java.util.Optional;

@Repository
public interface CategoriaComponenteRepository extends JpaRepository<CategoriaComponente, Integer> {
    
    Optional<CategoriaComponente> findByCategoriaComponente(String categoriaComponente);
}
