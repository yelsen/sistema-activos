package pe.edu.unasam.activos.modules.aplicativos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.aplicativos.domain.TipoAplicativo;

@Repository
public interface TipoAplicativoRepository extends JpaRepository<TipoAplicativo, Integer> {
    
}
