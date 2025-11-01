package pe.edu.unasam.activos.modules.ubicaciones.web;

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
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.config.PaginationProperties;
import pe.edu.unasam.activos.modules.ubicaciones.dto.OrigenDTO;
import pe.edu.unasam.activos.modules.ubicaciones.service.OrigenService;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/proveedores/origenes")
public class OrigenController {

    private final OrigenService origenService;
    private final PaginationProperties paginationProperties;

    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(java.util.stream.Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ORIGENES_ACCEDER')")
    public String listar(Model model,
                         @RequestParam(required = false) String query,
                         @RequestParam(required = false, defaultValue = "false") boolean fragment,
                         Pageable pageable) {
        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            Page<OrigenDTO.Response> origenPage = origenService.listar(query, normalizedPageable);

            model.addAttribute("origenPage", origenPage);
            model.addAttribute("query", query);
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());
            model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());
            model.addAttribute("url", "/proveedores/origenes");

            if (!model.containsAttribute("origen")) {
                model.addAttribute("origen", new OrigenDTO.Request());
            }

            if (!fragment) {
                return "proveedor/origen_activos/index";
            } else {
                return "proveedor/origen_activos/origen_activos-list :: origenList";
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar origenes", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ORIGENES_CREAR')")
    public String crear(@Valid @ModelAttribute("origen") OrigenDTO.Request request,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.origen", result);
            redirectAttributes.addFlashAttribute("origen", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/proveedores/origenes";
        }
        try {
            origenService.crear(request);
            redirectAttributes.addFlashAttribute("success", "Origen de activo creado correctamente.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", "Error al crear origen: " + ex.getMessage());
        }
        return "redirect:/proveedores/origenes";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('ORIGENES_EDITAR')")
    public String editar(@PathVariable("id") Integer id, Model model) {
        try {
            OrigenDTO.Response origen = origenService.getById(id);
            model.addAttribute("idOrigen", origen.getIdOrigen());
            model.addAttribute("origen", OrigenDTO.Request.builder()
                    .nombreOrigen(origen.getNombreOrigen())
                    .build());
            return "proveedor/origen_activos/modal/EditarModal :: editarForm";
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar origen: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ORIGENES_EDITAR')")
    public String actualizar(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("origen") OrigenDTO.Request request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.origen", result);
            redirectAttributes.addFlashAttribute("origen", request);
            redirectAttributes.addFlashAttribute("errorOrigenId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/proveedores/origenes";
        }
        try {
            origenService.actualizar(id, request);
            redirectAttributes.addFlashAttribute("success", "Origen actualizado correctamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorOrigenId", id);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorOrigenId", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error inesperado al actualizar el origen: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorOrigenId", id);
        }
        return "redirect:/proveedores/origenes";
}

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ORIGENES_ELIMINAR')")
    public String eliminar(@PathVariable("id") Integer id,
                           RedirectAttributes redirectAttributes) {
        try {
            OrigenDTO.Response resp = origenService.getById(id);
            origenService.eliminar(id);
            redirectAttributes.addFlashAttribute("success",
                    "Origen '" + resp.getNombreOrigen() + "' eliminado correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorOrigenDeleteId", id);
            redirectAttributes.addFlashAttribute("errorOrigenDeleteNombre", "ID " + id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/proveedores/origenes";
    }
}
