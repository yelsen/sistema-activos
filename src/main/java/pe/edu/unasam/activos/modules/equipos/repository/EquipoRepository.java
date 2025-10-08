package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.Equipo;
import pe.edu.unasam.activos.common.enums.EstadoEquipo;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    
    Optional<Equipo> findByCodigoEquipo(String codigoEquipo);
    
    List<Equipo> findByEstadoEquipo(EstadoEquipo estadoEquipo);
    
    List<Equipo> findByCategoriaEquipo_IdCategoriaEquipos(Integer idCategoriaEquipos);
    
    List<Equipo> findByMarca_IdMarca(Integer idMarca);
    
    @Query("SELECT e FROM Equipo e WHERE e.activo.oficina.idOficina = :idOficina")
    List<Equipo> findByOficina(@Param("idOficina") Integer idOficina);
    
    boolean existsByCodigoEquipo(String codigoEquipo);
    
    @Query("SELECT COUNT(e) FROM Equipo e WHERE e.categoriaEquipo.idCategoriaEquipos = :idCategoria")
    Long contarPorCategoria(@Param("idCategoria") Integer idCategoria);
}
