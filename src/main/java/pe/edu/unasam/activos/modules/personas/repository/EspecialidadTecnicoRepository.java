package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.personas.domain.EspecialidadTecnico;

import java.util.Optional;

@Repository
public interface EspecialidadTecnicoRepository extends JpaRepository<EspecialidadTecnico, Integer> {
    Optional<EspecialidadTecnico> findByNombreEspecialidad(String nombreEspecialidad);

    boolean existsByNombreEspecialidad(String nombreEspecialidad);
}
