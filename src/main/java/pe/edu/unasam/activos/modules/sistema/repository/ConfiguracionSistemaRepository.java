package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, Integer> {
    
    Optional<ConfiguracionSistema> findByClaveConfig(String claveConfig);

    List<ConfiguracionSistema> findByClaveConfigIn(Collection<String> claves);
    
    List<ConfiguracionSistema> findByCategoriaConfig(String categoriaConfig);
    
    boolean existsByClaveConfig(String claveConfig);

    boolean existsByClaveConfigAndIdConfiguracionSistemaNot(String claveConfig, Integer id);

    @Query("SELECT c FROM ConfiguracionSistema c WHERE " +
            "(:clave IS NULL OR LOWER(c.claveConfig) LIKE LOWER(CONCAT('%', :clave, '%'))) AND " +
            "(:categoria IS NULL OR LOWER(c.categoriaConfig) LIKE LOWER(CONCAT('%', :categoria, '%'))) " +
            "ORDER BY c.categoriaConfig, c.claveConfig ASC")
    Page<ConfiguracionSistema> searchByFilters(@Param("clave") String clave, @Param("categoria") String categoria, Pageable pageable);
}
