package pe.edu.unasam.activos.modules.ubicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Oficina;
import pe.edu.unasam.activos.common.enums.EstadoOficina;

import java.util.List;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Integer> {
    
    List<Oficina> findByDepartamento_IdDepartamento(Integer idDepartamento);
    
    List<Oficina> findByEstadoOficina(EstadoOficina estadoOficina);
    
    Page<Oficina> findByEstadoOficina(EstadoOficina estadoOficina, Pageable pageable);
    
    List<Oficina> findByTipoOficina_IdTipoOficina(Integer idTipoOficina);
    
    List<Oficina> findByNombreOficinaContainingIgnoreCase(String nombreOficina);
    
    Page<Oficina> findByNombreOficinaContainingIgnoreCase(String nombreOficina, Pageable pageable);

    Page<Oficina> findByNombreOficinaContainingIgnoreCaseAndEstadoOficina(String nombreOficina, EstadoOficina estadoOficina, Pageable pageable);

    boolean existsByNombreOficina(String nombreOficina);

    boolean existsByNombreOficinaIgnoreCase(String nombreOficina);
}
