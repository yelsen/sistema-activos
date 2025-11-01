package pe.edu.unasam.activos.modules.sistema.web;

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
import pe.edu.unasam.activos.config.PaginationProperties;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.modules.sistema.dto.AccionDTO;
import pe.edu.unasam.activos.modules.sistema.service.AccionService;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/seguridad/acciones")
public class AccionController {

    private final AccionService accionService;
    private final PaginationProperties paginationProperties;

    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(java.util.stream.Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ACCIONES_ACCEDER')")
    public String listar(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) {
        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            Page<AccionDTO.Response> accionPage = accionService.getAllAcciones(query, normalizedPageable);

            model.addAttribute("accionPage", accionPage);
            model.addAttribute("query", query);
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());
            model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());

            if (!model.containsAttribute("accion")) {
                model.addAttribute("accion", new AccionDTO.Request());
            }

            if (!fragment) {
                return "seguridad/acciones/index";
            } else {
                return "seguridad/acciones/acciones-list :: accionList";
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar acciones", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ACCIONES_CREAR')")
    public String crear(@Valid @ModelAttribute("accion") AccionDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.accion", result);
            redirectAttributes.addFlashAttribute("accion", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/seguridad/acciones";
        }
        try {
            accionService.createAccion(request);
            redirectAttributes.addFlashAttribute("success", "Acción creada correctamente.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", "Error al crear acción: " + ex.getMessage());
        }
        return "redirect:/seguridad/acciones";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('ACCIONES_EDITAR')")
    public String editar(@PathVariable("id") Integer id, Model model) {
        try {
            AccionDTO.Response accion = accionService.getAccionById(id)
                    .orElseThrow(() -> new NotFoundException("Acción no encontrada con ID: " + id));
            model.addAttribute("idAccion", accion.getIdAccion());
            model.addAttribute("accion", AccionDTO.Request.builder()
                    .nombreAccion(accion.getNombreAccion())
                    .codigoAccion(accion.getCodigoAccion())
                    .descripcionAccion(accion.getDescripcionAccion())
                    .build());
            return "seguridad/acciones/modal/EditarModal :: editarForm";
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar acción: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ACCIONES_EDITAR')")
    public String updateAccion(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("accion") AccionDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.accion", result);
            redirectAttributes.addFlashAttribute("accion", request);
            redirectAttributes.addFlashAttribute("errorAccionId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/seguridad/acciones";
        }
        try {
            accionService.updateAccion(id, request);
            redirectAttributes.addFlashAttribute("success", "Acción actualizada correctamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorAccionId", id);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorAccionId", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error inesperado al actualizar la acción: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorAccionId", id);
        }
        return "redirect:/seguridad/acciones";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ACCIONES_ELIMINAR')")
    public String eliminar(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        try {
            AccionDTO.Response resp = accionService.getAccionById(id)
                    .orElseThrow(() -> new NotFoundException("Acción no encontrada con ID: " + id));
            accionService.deleteAccion(id);
            redirectAttributes.addFlashAttribute("success",
                    "Acción '" + resp.getNombreAccion() + "' eliminada correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorAccionDeleteId", id);
            redirectAttributes.addFlashAttribute("errorAccionDeleteNombre", "ID " + id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/seguridad/acciones";
    }
}
