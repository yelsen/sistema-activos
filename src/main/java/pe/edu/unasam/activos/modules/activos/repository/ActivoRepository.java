package pe.edu.unasam.activos.modules.activos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.activos.domain.Activo;
import pe.edu.unasam.activos.common.enums.EstadoActivo;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivoRepository extends JpaRepository<Activo, Integer> {
    
    List<Activo> findByEstadoActivo(EstadoActivo estadoActivo);
    
    @Query("SELECT a FROM Activo a, AsignacionActivo aa WHERE aa.activo = a AND aa.oficina.idOficina = :idOficina")
    List<Activo> findByOficina_IdOficina(@Param("idOficina") Integer idOficina);
    
    List<Activo> findByProveedor_IdProveedor(Integer idProveedor);
    
    @Query("SELECT a FROM Activo a WHERE a.fechaExpiracion BETWEEN :inicio AND :fin")
    List<Activo> findActivosConGarantiaPorVencer(
            @Param("inicio") LocalDate inicio, 
            @Param("fin") LocalDate fin);
    
    @Query("SELECT a FROM Activo a WHERE a.fechaExpiracion < :fecha")
    List<Activo> findActivosConGarantiaVencida(@Param("fecha") LocalDate fecha);
    
    @Query("SELECT COUNT(a) FROM Activo a WHERE a.estadoActivo = :estado")
    Long contarPorEstado(@Param("estado") EstadoActivo estado);
}
