package pe.edu.unasam.activos.modules.licencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.licencias.domain.LicenciaAsignacion;
import pe.edu.unasam.activos.modules.licencias.domain.LicenciaAsignacionId;

import java.util.List;

@Repository
public interface LicenciaAsignacionRepository extends JpaRepository<LicenciaAsignacion, LicenciaAsignacionId> {
    
    List<LicenciaAsignacion> findByLicencia_IdLicencia(Integer idLicencia);
    
    List<LicenciaAsignacion> findByOficina_IdOficina(Integer idOficina);
}
