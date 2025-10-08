package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.Especificacion;

@Repository
public interface EspecificacionRepository extends JpaRepository<Especificacion, Integer> {
    
}
