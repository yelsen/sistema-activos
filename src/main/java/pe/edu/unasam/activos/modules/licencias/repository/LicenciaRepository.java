package pe.edu.unasam.activos.modules.licencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.licencias.domain.Licencia;
import pe.edu.unasam.activos.common.enums.EstadoLicencia;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Integer> {
    
    List<Licencia> findByEstadoLicencia(EstadoLicencia estadoLicencia);
    
    List<Licencia> findByAplicativo_IdAplicativo(Integer idAplicativo);
    
    @Query("SELECT l FROM Licencia l WHERE l.fechaExpiracion BETWEEN :inicio AND :fin")
    List<Licencia> findLicenciasPorVencer(
            @Param("inicio") LocalDate inicio, 
            @Param("fin") LocalDate fin);
    
    @Query("SELECT l FROM Licencia l WHERE l.fechaExpiracion < :fecha AND l.estadoLicencia = 'ACTIVA'")
    List<Licencia> findLicenciasVencidas(@Param("fecha") LocalDate fecha);
}
