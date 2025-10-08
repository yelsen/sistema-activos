package pe.edu.unasam.activos.modules.personas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.personas.domain.Persona;
import pe.edu.unasam.activos.common.enums.EstadoPersona;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String>, JpaSpecificationExecutor<Persona> {
    
    List<Persona> findByEstado(EstadoPersona estado);
    
    List<Persona> findByApellidosContainingIgnoreCaseOrNombresContainingIgnoreCase(
            String apellidos, String nombres);
    
    @Query("SELECT p FROM Persona p WHERE CONCAT(p.apellidos, ' ', p.nombres) LIKE %:nombreCompleto%")
    List<Persona> buscarPorNombreCompleto(String nombreCompleto);
    
    boolean existsByEmail(String email);
}
