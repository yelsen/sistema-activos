package pe.edu.unasam.activos.modules.servidores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.servidores.domain.Servidor;
import java.util.Optional;

@Repository
public interface ServidorRepository extends JpaRepository<Servidor, Integer> {
    
    Optional<Servidor> findByNombreServidor(String nombreServidor);
    
}
