package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.sistema.domain.ModuloSistema;
import pe.edu.unasam.activos.common.enums.EstadoModulo;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuloSistemaRepository extends JpaRepository<ModuloSistema, Integer> {

        List<ModuloSistema> findAllByOrderByOrdenModuloAsc();

        Page<ModuloSistema> findAllByOrderByOrdenModuloAsc(Pageable pageable);

        Optional<ModuloSistema> findByNombreModulo(String nombreModulo);

        

        List<ModuloSistema> findByEstadoModuloOrderByOrdenModuloAsc(EstadoModulo estado);

        @Query("SELECT m FROM ModuloSistema m WHERE m.estadoModulo = 'ACTIVO' ORDER BY m.ordenModulo ASC")
        List<ModuloSistema> findAllActivos();

        

        Page<ModuloSistema> findByNombreModuloContainingIgnoreCaseOrderByOrdenModuloAsc(
                        String nombre, Pageable pageable);

        Page<ModuloSistema> findByDescripcionModuloContainingIgnoreCaseOrderByOrdenModuloAsc(
                        String descripcion, Pageable pageable);

        @Query("SELECT m FROM ModuloSistema m WHERE " +
                        "(:nombre IS NULL OR LOWER(m.nombreModulo) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
                        "(:estado IS NULL OR m.estadoModulo = :estado) AND " +
                        "(:ruta IS NULL OR LOWER(m.rutaModulo) LIKE LOWER(CONCAT('%', :ruta, '%'))) " +
                        "ORDER BY m.ordenModulo ASC")
        Page<ModuloSistema> buscarConFiltros(
                        @Param("nombre") String nombre,
                        @Param("estado") EstadoModulo estado,
                        @Param("ruta") String ruta,
                        Pageable pageable);

        boolean existsByNombreModulo(String nombreModulo);

        boolean existsByNombreModuloAndIdModuloSistemasNot(String nombreModulo, Integer id);

        boolean existsByRutaModulo(String rutaModulo);

        boolean existsByRutaModuloAndIdModuloSistemasNot(String rutaModulo, Integer id);

        @Query("SELECT COALESCE(MAX(m.ordenModulo), 0) FROM ModuloSistema m")
        Integer findMaxOrden();

        
}
