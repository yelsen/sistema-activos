package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.EquipoEspecificacion;
import pe.edu.unasam.activos.modules.equipos.domain.EquipoEspecificacionId;

import java.util.List;

@Repository
public interface EquipoEspecificacionRepository extends JpaRepository<EquipoEspecificacion, EquipoEspecificacionId> {
    
    List<EquipoEspecificacion> findByEquipo_IdEquipo(Integer idEquipo);
    
    List<EquipoEspecificacion> findByEspecificacion_IdEspecificacion(Integer idEspecificacion);
}
