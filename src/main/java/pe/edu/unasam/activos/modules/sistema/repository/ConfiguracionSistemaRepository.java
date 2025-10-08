package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
