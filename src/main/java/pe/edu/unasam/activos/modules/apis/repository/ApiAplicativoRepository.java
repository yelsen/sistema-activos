package pe.edu.unasam.activos.modules.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.apis.domain.ApiAplicativo;
import pe.edu.unasam.activos.modules.apis.domain.ApiAplicativoId;

@Repository
public interface ApiAplicativoRepository extends JpaRepository<ApiAplicativo, ApiAplicativoId> {
  
}
