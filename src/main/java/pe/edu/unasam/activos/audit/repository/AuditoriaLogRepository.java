package pe.edu.unasam.activos.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.audit.domain.AuditoriaLog;
import pe.edu.unasam.activos.common.enums.AccionAuditoria;

import java.util.List;

@Repository
public interface AuditoriaLogRepository extends JpaRepository<AuditoriaLog, Integer>, JpaSpecificationExecutor<AuditoriaLog> {
    
    List<AuditoriaLog> findByTablaAfectada(String tablaAfectada);
    
    List<AuditoriaLog> findByAccion(AccionAuditoria accion);

    @Query("SELECT DISTINCT a.tablaAfectada FROM AuditoriaLog a ORDER BY a.tablaAfectada")
    List<String> findDistinctTablaAfectada();
}
