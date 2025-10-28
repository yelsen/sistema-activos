package pe.edu.unasam.activos.modules.personas.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pe.edu.unasam.activos.common.enums.EstadoResponsable;
import pe.edu.unasam.activos.common.enums.Genero;
import pe.edu.unasam.activos.config.PaginationProperties;
import pe.edu.unasam.activos.modules.personas.dto.ResponsableDTO;
import pe.edu.unasam.activos.modules.personas.service.CargoService;
import pe.edu.unasam.activos.modules.personas.service.ResponsableService;
import pe.edu.unasam.activos.modules.ubicaciones.service.OficinaService;
import java.util.stream.Collectors;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.common.exception.BusinessException;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/organizacion/responsables")
public class ResponsableController {
    private final ResponsableService responsableService;
    private final CargoService cargoService;
    private final OficinaService oficinaService;
    private final PaginationProperties paginationProperties;

    // --- Métodos de ayuda para manejo de errores ---
    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('RESPONSABLES_ACCEDER')")
    public String listar(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) EstadoResponsable estado,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) {

        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            Page<ResponsableDTO.Response> responsablePage = responsableService.getAllResponsables(query, estado,
                    normalizedPageable);

            model.addAttribute("responsablePage", responsablePage);
            model.addAttribute("query", query);
            model.addAttribute("estado", estado);
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());

            // Construir queryParams para paginación
            UriComponentsBuilder queryParamsBuilder = UriComponentsBuilder.newInstance();
            if (query != null && !query.isBlank()) {
                queryParamsBuilder.queryParam("query", query);
            }
            if (estado != null) {
                queryParamsBuilder.queryParam("estado", estado.name());
            }
            model.addAttribute("queryParams", queryParamsBuilder.build().getQuery());

            if (!model.containsAttribute("responsable")) {
                model.addAttribute("responsable", new ResponsableDTO.Request());
            }
            
            model.addAttribute("generos", Genero.values());
            model.addAttribute("cargos", cargoService.getAllCargosAsList());
            model.addAttribute("oficinas", oficinaService.getAllOficinasAsList());
            model.addAttribute("estadoResponsables", EstadoResponsable.values());
            model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());

            if (!fragment) {
                return "organizacion/responsables/index";
            } else {
                return "organizacion/responsables/responsables-list :: responsableList";
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar responsables", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RESPONSABLES_CREAR')")
    public String crear(@Valid @ModelAttribute("responsable") ResponsableDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.responsable", result);
            redirectAttributes.addFlashAttribute("responsable", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/organizacion/responsables";
        }
        try {
            responsableService.createResponsable(request);
            redirectAttributes.addFlashAttribute("success", "Responsable creado correctamente.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", "Error al crear responsable: " + ex.getMessage());
        }
        return "redirect:/organizacion/responsables";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('RESPONSABLES_EDITAR')")
    public String editar(@PathVariable("id") Integer id, Model model) {
        try {
            var editForm = responsableService.getEditForm(id);

            java.util.List<pe.edu.unasam.activos.modules.personas.dto.CargoDTO.Response> cargos = cargoService
                    .getAllCargosAsList();
            java.util.List<pe.edu.unasam.activos.modules.ubicaciones.dto.OficinaDTO.Response> oficinas = oficinaService
                    .getAllOficinasAsList();

            model.addAttribute("responsable", editForm);
            model.addAttribute("cargos", cargos);
            model.addAttribute("oficinas", oficinas);
            model.addAttribute("generos", pe.edu.unasam.activos.common.enums.Genero.values());

            return "organizacion/responsables/modal/EditarModal :: editarForm";
        } catch (pe.edu.unasam.activos.common.exception.NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (pe.edu.unasam.activos.common.exception.BusinessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar responsable: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('RESPONSABLES_EDITAR')")
    public String updateResponsable(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("responsable") ResponsableDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.responsable", result);
            redirectAttributes.addFlashAttribute("responsable", request);
            redirectAttributes.addFlashAttribute("errorResponsableId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/organizacion/responsables";
        }
        try {
            responsableService.updateResponsable(id, request);
            redirectAttributes.addFlashAttribute("success", "Responsable actualizado correctamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorResponsableId", id);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorResponsableId", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error inesperado al actualizar el responsable: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorResponsableId", id);
        }
        return "redirect:/organizacion/responsables";
    }

    @PostMapping("/{id}/cambiar-estado")
    @PreAuthorize("hasAuthority('RESPONSABLES_EDITAR')")
    public String toggleEstado(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            responsableService.toggleEstado(id);
            redirectAttributes.addFlashAttribute("success", "Estado del responsable cambiado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/organizacion/responsables";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('RESPONSABLES_ELIMINAR')")
    public String eliminar(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        try {
            ResponsableDTO.Response resp = responsableService.getResponsableById(id);
            responsableService.deleteResponsable(id);
            redirectAttributes.addFlashAttribute("success",
                    "Responsable '" + resp.getNombrePersona() + "' eliminado correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorResponsableDeleteId", id);
            redirectAttributes.addFlashAttribute("errorResponsableDeleteNombre", "ID " + id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/organizacion/responsables";
    }
}
