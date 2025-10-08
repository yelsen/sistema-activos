package pe.edu.unasam.activos.modules.mantenimientos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.mantenimientos.domain.Tecnico;

import java.util.List;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
    
    List<Tecnico> findByEspecialidad_IdEspecialidad(Integer idEspecialidad);
}
