package pe.edu.unasam.activos.modules.proveedores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.proveedores.domain.Proveedor;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    
    List<Proveedor> findByNombreProveedorContainingIgnoreCase(String nombreProveedor);
}
