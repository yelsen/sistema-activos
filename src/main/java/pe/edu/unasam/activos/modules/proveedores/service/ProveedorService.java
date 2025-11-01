package pe.edu.unasam.activos.modules.proveedores.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.enums.EstadoProveedor;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.proveedores.domain.Proveedor;
import pe.edu.unasam.activos.modules.proveedores.dto.ProveedorDTO;
import pe.edu.unasam.activos.modules.proveedores.repository.ProveedorRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Transactional(readOnly = true)
    public Page<ProveedorDTO.Response> listar(String query, Pageable pageable) {
        String q = trimToNull(query);
        if (q != null) {
            return proveedorRepository
                    .findByNombreProveedorContainingIgnoreCaseOrRucProveedorContaining(q, q, pageable)
                    .map(this::toDto);
        }
        return proveedorRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public ProveedorDTO.Response getById(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con ID: " + id));
        return toDto(proveedor);
    }

    public ProveedorDTO.Response crear(ProveedorDTO.Request request) {
        String ruc = trimToNull(request.getRucProveedor());
        String nombre = trimToNull(request.getNombreProveedor());
        if (ruc == null) {
            throw new BusinessException("El RUC del proveedor es obligatorio.");
        }
        if (nombre == null) {
            throw new BusinessException("El nombre del proveedor es obligatorio.");
        }
        if (proveedorRepository.existsByRucProveedor(ruc)) {
            throw new BusinessException("Ya existe un proveedor con el RUC '" + ruc + "'.");
        }
        if (proveedorRepository.existsByNombreProveedorIgnoreCase(nombre)) {
            throw new BusinessException("Ya existe un proveedor con el nombre '" + nombre + "'.");
        }

        Proveedor proveedor = Proveedor.builder()
                .rucProveedor(ruc)
                .nombreProveedor(nombre)
                .razonSocial(request.getRazonSocial())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .estadoProveedor(request.getEstadoProveedor() != null ? request.getEstadoProveedor() : EstadoProveedor.ACTIVO)
                .build();

        return toDto(proveedorRepository.save(proveedor));
    }

    public ProveedorDTO.Response actualizar(Integer id, ProveedorDTO.Request request) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con ID: " + id));

        String ruc = trimToNull(request.getRucProveedor());
        String nombre = trimToNull(request.getNombreProveedor());
        if (ruc == null) {
            throw new BusinessException("El RUC del proveedor es obligatorio.");
        }
        if (nombre == null) {
            throw new BusinessException("El nombre del proveedor es obligatorio.");
        }

        proveedorRepository.findByRucProveedor(ruc).ifPresent(existing -> {
            if (!existing.getIdProveedor().equals(id)) {
                throw new BusinessException("Ya existe un proveedor con el RUC '" + ruc + "'.");
            }
        });
        if (!proveedor.getNombreProveedor().equalsIgnoreCase(nombre)
                && proveedorRepository.existsByNombreProveedorIgnoreCase(nombre)) {
            throw new BusinessException("Ya existe un proveedor con el nombre '" + nombre + "'.");
        }

        proveedor.setRucProveedor(ruc);
        proveedor.setNombreProveedor(nombre);
        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setEmail(request.getEmail());
        
        return toDto(proveedorRepository.save(proveedor));
    }

    public void eliminar(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con ID: " + id));
        // Baja lógica: marcar como INACTIVO en lugar de eliminar
        proveedor.setEstadoProveedor(EstadoProveedor.INACTIVO);
        proveedorRepository.save(proveedor);
    }

    private ProveedorDTO.Response toDto(Proveedor proveedor) {
        return ProveedorDTO.Response.builder()
                .idProveedor(proveedor.getIdProveedor())
                .rucProveedor(proveedor.getRucProveedor())
                .nombreProveedor(proveedor.getNombreProveedor())
                .razonSocial(proveedor.getRazonSocial())
                .direccion(proveedor.getDireccion())
                .telefono(proveedor.getTelefono())
                .email(proveedor.getEmail())
                .estadoProveedor(proveedor.getEstadoProveedor())
                .build();
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
