package pe.edu.unasam.activos.modules.ubicaciones.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.ubicaciones.domain.Departamento;

import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer>  {
    
    Optional<Departamento> findByNombreDepartamento(String nombreDepartamento);
    
    boolean existsByNombreDepartamento(String nombreDepartamento);

    Page<Departamento> findByNombreDepartamentoContainingIgnoreCase(String query, Pageable pageable);
}
