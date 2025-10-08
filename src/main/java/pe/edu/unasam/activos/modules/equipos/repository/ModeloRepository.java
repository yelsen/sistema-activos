package pe.edu.unasam.activos.modules.equipos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.equipos.domain.Modelo;

import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Integer> {
    
    List<Modelo> findByNombreModeloContainingIgnoreCase(String nombreModelo);
    
    List<Modelo> findByAnoLanzamiento(Integer anoLanzamiento);
}
