package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.Componente;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComponentesRepository extends JpaRepository<Componente, Integer> {
    Optional<Componente> findByNombreComponente(String nombreComponente);

    List<Componente> findByCategoriaComponente_CategoriaComponente(String categoriaComponente);

    Optional<Componente> findByNombreComponenteAndCategoriaComponente_CategoriaComponente(
            String nombreComponente, String categoriaComponente);
}
