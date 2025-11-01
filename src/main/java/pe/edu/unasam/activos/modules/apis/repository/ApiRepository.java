package pe.edu.unasam.activos.modules.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.apis.domain.Api;
import java.util.Optional;

@Repository
public interface ApiRepository extends JpaRepository<Api, Integer> {
    
    Optional<Api> findByNombreApi(String nombreApi);
}
