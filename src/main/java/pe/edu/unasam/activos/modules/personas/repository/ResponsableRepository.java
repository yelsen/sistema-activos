package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.unasam.activos.common.enums.EstadoResponsable;
import pe.edu.unasam.activos.modules.personas.domain.Responsable;

import java.util.List;

@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Integer> {

  List<Responsable> findByOficina_IdOficina(Integer idOficina);

  List<Responsable> findByEstadoResponsable(EstadoResponsable estadoResponsable);


  List<Responsable> findByPersona_Dni(String dni);

  @Query("""
          SELECT r FROM Responsable r
          LEFT JOIN r.persona p
          LEFT JOIN r.cargo c
          LEFT JOIN r.oficina o
          WHERE (:estado IS NULL OR r.estadoResponsable = :estado)
            AND (
                  :q IS NULL OR :q = '' OR
                  LOWER(CONCAT(p.nombres, ' ', p.apellidos)) LIKE LOWER(CONCAT('%', :q, '%')) OR
                  p.dni LIKE CONCAT('%', :q, '%') OR
                  LOWER(c.nombreCargo) LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(o.nombreOficina) LIKE LOWER(CONCAT('%', :q, '%'))
                )
          ORDER BY r.createdAt DESC
      """)
  Page<Responsable> findAllWithFilters(@Param("q") String query,
      @Param("estado") EstadoResponsable estado,
      Pageable pageable);
}
