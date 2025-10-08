package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.Unidad;
import pe.edu.unasam.activos.common.enums.TipoUnidad;

import java.util.List;

@Repository
public interface UnidadRepository extends JpaRepository<Unidad, Integer> {
    
    List<Unidad> findByTipoUnidad(TipoUnidad tipoUnidad);
}
