package pe.edu.unasam.activos.modules.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.apis.domain.Api;
import pe.edu.unasam.activos.common.enums.MetodoAutenticacion;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiRepository extends JpaRepository<Api, Integer> {
    
    List<Api> findByNombreApiContainingIgnoreCase(String nombreApi);
    
    Optional<Api> findByUrlApi(String urlApi);
    
    List<Api> findByMetodoAutenticacion(MetodoAutenticacion metodoAutenticacion);
    
    @Query("SELECT a FROM Api a WHERE a.versionApi = :version")
    List<Api> findByVersion(@Param("version") String version);
    
    boolean existsByNombreApi(String nombreApi);
}
