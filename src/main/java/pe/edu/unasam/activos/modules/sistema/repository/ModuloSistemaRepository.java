package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.sistema.domain.ModuloSistema;
import pe.edu.unasam.activos.common.enums.EstadoModulo;

import java.util.List;

@Repository
public interface ModuloSistemaRepository extends JpaRepository<ModuloSistema, Integer> {

    List<ModuloSistema> findByEstadoModuloOrderByOrdenModuloAsc(EstadoModulo estadoModulo);

    List<ModuloSistema> findAllByOrderByOrdenModuloAsc();

    boolean existsByRutaModulo(String rutaModulo);
}
