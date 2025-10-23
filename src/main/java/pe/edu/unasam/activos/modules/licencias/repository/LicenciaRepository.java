package pe.edu.unasam.activos.modules.licencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.edu.unasam.activos.modules.licencias.domain.Licencia;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Integer> {
    

}
