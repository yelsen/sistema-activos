package pe.edu.unasam.activos.modules.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.sistema.domain.SesionUsuario;
import pe.edu.unasam.activos.common.enums.EstadoSesion;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesionUsuarioRepository extends JpaRepository<SesionUsuario, Integer>, JpaSpecificationExecutor<SesionUsuario> {
    
    Optional<SesionUsuario> findByTokenSesion(String tokenSesion);
    
    List<SesionUsuario> findByUsuario_IdUsuarioAndEstadoSesion(Integer idUsuario, EstadoSesion estadoSesion);
    
    List<SesionUsuario> findByEstadoSesion(EstadoSesion estadoSesion);
    
    void deleteByUsuario_IdUsuario(Integer idUsuario);
}
