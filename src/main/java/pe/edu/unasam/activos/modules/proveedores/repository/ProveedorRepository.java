package pe.edu.unasam.activos.modules.proveedores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pe.edu.unasam.activos.modules.proveedores.domain.Proveedor;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    
    List<Proveedor> findByNombreProveedorContainingIgnoreCase(String nombreProveedor);

    Page<Proveedor> findByNombreProveedorContainingIgnoreCase(String nombreProveedor, Pageable pageable);

    Page<Proveedor> findByNombreProveedorContainingIgnoreCaseOrRucProveedorContaining(String nombreProveedor, String rucProveedor, Pageable pageable);

    Optional<Proveedor> findByRucProveedor(String rucProveedor);

    boolean existsByRucProveedor(String rucProveedor);

    boolean existsByNombreProveedorIgnoreCase(String nombreProveedor);
}
