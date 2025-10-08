package pe.edu.unasam.activos.modules.aplicativos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.aplicativos.domain.Aplicativo;
import pe.edu.unasam.activos.common.enums.EstadoAplicativo;

import java.util.List;

@Repository
public interface AplicativoRepository extends JpaRepository<Aplicativo, Integer> {
    
    List<Aplicativo> findByEstadoAplicativo(EstadoAplicativo estadoAplicativo);
    
    List<Aplicativo> findByTipoAplicativo_IdTipoAplicativo(Integer idTipoAplicativo);
    
    List<Aplicativo> findByNombreAplicativoContainingIgnoreCase(String nombreAplicativo);
    
    @Query("SELECT DISTINCT a.versionActual FROM Aplicativo a WHERE a.idAplicativo = :idAplicativo")
    List<String> findVersionesByAplicativo(Integer idAplicativo);
}
