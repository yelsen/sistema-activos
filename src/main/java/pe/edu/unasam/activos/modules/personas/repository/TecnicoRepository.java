package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.common.enums.EstadoTecnico;
import pe.edu.unasam.activos.modules.personas.domain.Tecnico;

import java.util.List;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

    List<Tecnico> findByEspecialidadTecnico_IdEspecialidadTecnico(Integer idEspecialidad);

    List<Tecnico> findByPersona_Dni(String dni);

    boolean existsByPersona_Dni(String dni);

    @Query("""
            SELECT t FROM Tecnico t LEFT JOIN t.persona p LEFT JOIN t.especialidadTecnico e
            WHERE (:estado IS NULL OR t.estadoTecnico = :estado)
            AND (:query IS NULL OR :query = '' OR LOWER(p.dni) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(CONCAT(p.nombres, ' ', p.apellidos)) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(e.nombreEspecialidad) LIKE LOWER(CONCAT('%', :query, '%')))
            """)
    Page<Tecnico> findAllWithFilters(@Param("query") String query, @Param("estado") EstadoTecnico estado,
            Pageable pageable);
}
