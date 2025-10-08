package pe.edu.unasam.activos.modules.ubicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import pe.edu.unasam.activos.common.enums.EstadoOficina;

import java.util.List;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Integer> {
    
    List<Oficina> findByDepartamento_IdDepartamento(Integer idDepartamento);
    
    List<Oficina> findByEstadoOficina(EstadoOficina estadoOficina);
    
    List<Oficina> findByTipoOficina_IdTipoOficina(Integer idTipoOficina);
    
    List<Oficina> findByNombreOficinaContainingIgnoreCase(String nombreOficina);
}
