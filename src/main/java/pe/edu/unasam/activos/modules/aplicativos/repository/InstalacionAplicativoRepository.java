package pe.edu.unasam.activos.modules.aplicativos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.aplicativos.domain.InstalacionAplicativo;
import pe.edu.unasam.activos.modules.aplicativos.domain.InstalacionAplicativoId;

import java.util.List;

@Repository
public interface InstalacionAplicativoRepository extends JpaRepository<InstalacionAplicativo, InstalacionAplicativoId> {
    
    List<InstalacionAplicativo> findByEquipo_IdEquipo(Integer idEquipo);
    
    List<InstalacionAplicativo> findByAplicativo_IdAplicativo(Integer idAplicativo);
}
