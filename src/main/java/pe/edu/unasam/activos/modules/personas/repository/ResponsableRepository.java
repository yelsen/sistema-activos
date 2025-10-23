package pe.edu.unasam.activos.modules.personas.repository;

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

    @Query("SELECT r FROM Responsable r WHERE r.oficina.idOficina = :idOficina AND r.esResponsablePrincipal = true AND r.estadoResponsable = 'ACTIVO'")
    List<Responsable> findResponsablesPrincipalesByOficina(@Param("idOficina") Integer idOficina);

    List<Responsable> findByPersona_NumeroDocumento(String numeroDocumento);
}
