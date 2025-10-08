package pe.edu.unasam.activos.modules.apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.apis.domain.ApiAplicativo;
import pe.edu.unasam.activos.modules.apis.domain.ApiAplicativoId;
import pe.edu.unasam.activos.common.enums.EstadoIntegracion;
import pe.edu.unasam.activos.common.enums.UsoApi;

import java.util.List;

@Repository
public interface ApiAplicativoRepository extends JpaRepository<ApiAplicativo, ApiAplicativoId> {
    
    List<ApiAplicativo> findByAplicativo_IdAplicativo(Integer idAplicativo);
    
    List<ApiAplicativo> findByApi_IdApi(Integer idApi);
    
    List<ApiAplicativo> findByEstadoIntegracion(EstadoIntegracion estadoIntegracion);
    
    List<ApiAplicativo> findByUso(UsoApi uso);
    
    @Query("SELECT aa FROM ApiAplicativo aa WHERE aa.aplicativo.idAplicativo = :idAplicativo AND aa.estadoIntegracion = :estado")
    List<ApiAplicativo> findByAplicativoAndEstado(
            @Param("idAplicativo") Integer idAplicativo, 
            @Param("estado") EstadoIntegracion estado);
    
    @Query("SELECT COUNT(aa) FROM ApiAplicativo aa WHERE aa.api.idApi = :idApi")
    Long countIntegracionesByApi(@Param("idApi") Integer idApi);

    @Query("SELECT aa.api.idApi, COUNT(aa.api.idApi) FROM ApiAplicativo aa WHERE aa.api.idApi IN :apiIds GROUP BY aa.api.idApi")
    List<Object[]> countIntegracionesByApiIds(@Param("apiIds") List<Integer> apiIds);
}
