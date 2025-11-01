package pe.edu.unasam.activos.modules.servidores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.servidores.domain.Recurso;
import java.util.Optional;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Integer> {
    
    Optional<Recurso> findByNombreRecurso(String nombreRecurso);

}
