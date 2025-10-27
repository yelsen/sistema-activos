package pe.edu.unasam.activos.modules.ubicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.ubicaciones.domain.TipoOficina;

import java.util.Optional;

@Repository
public interface TipoOficinaRepository extends JpaRepository<TipoOficina, Integer> {
    
    Optional<TipoOficina> findByTipoOficina(String tipoOficina);

    boolean existsByTipoOficina(String tipoOficina);
}
