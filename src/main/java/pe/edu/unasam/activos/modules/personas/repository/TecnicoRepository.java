package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.personas.domain.Tecnico;

import java.util.List;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
    
    List<Tecnico> findByEspecialidadTecnico_IdEspecialidadTecnico(Integer idEspecialidad);

    List<Tecnico> findByPersona_Dni(String dni);
}

