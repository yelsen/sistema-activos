package pe.edu.unasam.activos.modules.mantenimientos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.mantenimientos.domain.Mantenimiento;
import pe.edu.unasam.activos.common.enums.EstadoMantenimiento;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {
    
    List<Mantenimiento> findByEstadoMantenimiento(EstadoMantenimiento estadoMantenimiento);
    
    List<Mantenimiento> findByActivo_IdActivo(Integer idActivo);
    
    List<Mantenimiento> findByTecnico_IdTecnico(Integer idTecnico);
    
    @Query("SELECT m FROM Mantenimiento m WHERE m.fechaProgramada BETWEEN :inicio AND :fin")
    List<Mantenimiento> findMantenimientosProgramados(
            @Param("inicio") LocalDate inicio, 
            @Param("fin") LocalDate fin);
    
    @Query("SELECT m FROM Mantenimiento m WHERE m.fechaProgramada < :fecha AND m.estadoMantenimiento = 'PENDIENTE'")
    List<Mantenimiento> findMantenimientosVencidos(@Param("fecha") LocalDate fecha);
}
