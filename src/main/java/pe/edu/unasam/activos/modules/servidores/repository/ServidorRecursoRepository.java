package pe.edu.unasam.activos.modules.servidores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.servidores.domain.ServidorRecurso;
import pe.edu.unasam.activos.modules.servidores.domain.ServidorRecursoId;

import java.util.List;

@Repository
public interface ServidorRecursoRepository extends JpaRepository<ServidorRecurso, ServidorRecursoId> {
    
    List<ServidorRecurso> findByServidor_IdServidor(Integer idServidor);

    @Query("SELECT COUNT(sr) FROM ServidorRecurso sr WHERE sr.servidor IS NOT NULL AND sr.recurso IS NOT NULL")
    long countValid();
}
