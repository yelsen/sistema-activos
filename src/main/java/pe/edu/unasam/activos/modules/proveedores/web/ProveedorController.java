package pe.edu.unasam.activos.modules.proveedores.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.unasam.activos.common.enums.EstadoProveedor;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.config.PaginationProperties;
import pe.edu.unasam.activos.modules.proveedores.dto.ProveedorDTO;
import pe.edu.unasam.activos.modules.proveedores.service.ProveedorService;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final PaginationProperties paginationProperties;

    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(java.util.stream.Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PROVEEDORES_ACCEDER')")
    public String listar(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) {
        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            Page<ProveedorDTO.Response> proveedorPage = proveedorService.listar(query, normalizedPageable);

            model.addAttribute("proveedorPage", proveedorPage);
            model.addAttribute("query", query);
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());
            model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());
            model.addAttribute("estadoProveedor", EstadoProveedor.values());
            model.addAttribute("url", "/proveedores");

            if (!model.containsAttribute("proveedor")) {
                model.addAttribute("proveedor", new ProveedorDTO.Request());
            }

            if (!fragment) {
                return "proveedor/proveedores/index";
            } else {
                return "proveedor/proveedores/proveedores-list :: proveedorList";
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar proveedores", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PROVEEDORES_CREAR')")
    public String crear(@Valid @ModelAttribute("proveedor") ProveedorDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.proveedor", result);
            redirectAttributes.addFlashAttribute("proveedor", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/proveedores";
        }
        try {
            proveedorService.crear(request);
            redirectAttributes.addFlashAttribute("success", "Proveedor creado correctamente.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", "Error al crear proveedor: " + ex.getMessage());
        }
        return "redirect:/proveedores";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('PROVEEDORES_EDITAR')")
    public String editar(@PathVariable("id") Integer id, Model model) {
        try {
            ProveedorDTO.Response proveedor = proveedorService.getById(id);
            model.addAttribute("idProveedor", proveedor.getIdProveedor());
            model.addAttribute("proveedor", ProveedorDTO.Request.builder()
                    .rucProveedor(proveedor.getRucProveedor())
                    .nombreProveedor(proveedor.getNombreProveedor())
                    .razonSocial(proveedor.getRazonSocial())
                    .direccion(proveedor.getDireccion())
                    .telefono(proveedor.getTelefono())
                    .email(proveedor.getEmail())
                    .estadoProveedor(proveedor.getEstadoProveedor())
                    .build());
            model.addAttribute("estadoProveedor", EstadoProveedor.values());
            return "proveedor/proveedores/modal/EditarModal :: editarForm";
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar proveedor: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('PROVEEDORES_EDITAR')")
    public String actualizar(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("proveedor") ProveedorDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.proveedor", result);
            redirectAttributes.addFlashAttribute("proveedor", request);
            redirectAttributes.addFlashAttribute("errorProveedorId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/proveedores";
        }
        try {
            proveedorService.actualizar(id, request);
            redirectAttributes.addFlashAttribute("success", "Proveedor actualizado correctamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorProveedorId", id);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorProveedorId", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error inesperado al actualizar el proveedor: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorProveedorId", id);
        }
        return "redirect:/proveedores";
}

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PROVEEDORES_ELIMINAR')")
    public String eliminar(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        try {
            ProveedorDTO.Response resp = proveedorService.getById(id);
            proveedorService.eliminar(id);
            redirectAttributes.addFlashAttribute("success",
                    "Proveedor '" + resp.getNombreProveedor() + "' eliminado correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorProveedorDeleteId", id);
            redirectAttributes.addFlashAttribute("errorProveedorDeleteNombre", "ID " + id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/proveedores";
    }
}
