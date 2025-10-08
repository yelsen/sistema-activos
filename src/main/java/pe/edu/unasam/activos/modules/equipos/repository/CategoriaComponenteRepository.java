package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.CategoriaComponente;

@Repository
public interface CategoriaComponenteRepository extends JpaRepository<CategoriaComponente, Integer> {
    
}
