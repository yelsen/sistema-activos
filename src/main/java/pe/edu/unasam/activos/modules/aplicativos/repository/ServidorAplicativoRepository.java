package pe.edu.unasam.activos.modules.aplicativos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.aplicativos.domain.ServidorAplicativo;
import pe.edu.unasam.activos.modules.aplicativos.domain.ServidorAplicativoId;

import java.util.List;

@Repository
public interface ServidorAplicativoRepository extends JpaRepository<ServidorAplicativo, ServidorAplicativoId> {
    
    List<ServidorAplicativo> findByServidor_IdServidor(Integer idServidor);
    
    List<ServidorAplicativo> findByAplicativo_IdAplicativo(Integer idAplicativo);
}
