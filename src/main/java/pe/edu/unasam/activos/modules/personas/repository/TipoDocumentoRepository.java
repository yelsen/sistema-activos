package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.personas.domain.TipoDocumento;

import java.util.Optional;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer>, JpaSpecificationExecutor<TipoDocumento> {
    
    Optional<TipoDocumento> findByTipoDocumento(String tipoDocumento);
}
