package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.Unidad;
import pe.edu.unasam.activos.common.enums.TipoUnidad;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadRepository extends JpaRepository<Unidad, Integer> {

    Optional<Unidad> findByNombreUnidad(String nombreUnidad);

    Optional<Unidad> findBySimboloUnidad(String simboloUnidad);

    List<Unidad> findByTipoUnidad(TipoUnidad tipoUnidad);

}
