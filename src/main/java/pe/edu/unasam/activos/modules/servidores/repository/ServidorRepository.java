package pe.edu.unasam.activos.modules.servidores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.servidores.domain.Servidor;
import pe.edu.unasam.activos.common.enums.TipoServidor;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServidorRepository extends JpaRepository<Servidor, Integer> {
    
    Optional<Servidor> findByDireccionIp(String direccionIp);
    
    List<Servidor> findByTipoServidor(TipoServidor tipoServidor);
    
    List<Servidor> findByRolServidor_IdRolServidor(Integer idRolServidor);
    
    List<Servidor> findByNombreServidorContainingIgnoreCase(String nombreServidor);
}
