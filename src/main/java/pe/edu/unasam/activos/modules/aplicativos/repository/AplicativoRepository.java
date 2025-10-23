package pe.edu.unasam.activos.modules.aplicativos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.aplicativos.domain.Aplicativo;


@Repository
public interface AplicativoRepository extends JpaRepository<Aplicativo, Integer> {
    
    
}
