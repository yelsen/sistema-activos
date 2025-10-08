package pe.edu.unasam.activos.modules.servidores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.servidores.domain.RolServidor;

@Repository
public interface RolServidorRepository extends JpaRepository<RolServidor, Integer> {
    
}
