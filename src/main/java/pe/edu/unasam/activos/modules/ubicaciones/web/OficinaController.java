package pe.edu.unasam.activos.modules.ubicaciones.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

import pe.edu.unasam.activos.config.PaginationProperties;
import pe.edu.unasam.activos.modules.ubicaciones.service.OficinaService;
import pe.edu.unasam.activos.modules.ubicaciones.dto.OficinaDTO;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.common.exception.BusinessException;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/organizacion/oficinas")
public class OficinaController {

    private final OficinaService oficinaService;
    private final PaginationProperties paginationProperties;

    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(java.util.stream.Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('OFICINAS_ACCEDER')")
    public String listar(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) pe.edu.unasam.activos.common.enums.EstadoOficina estado,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) {

        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            Page<OficinaDTO.Response> oficinaPage = oficinaService.listarPaginado(query, estado, normalizedPageable);

            model.addAttribute("oficinaPage", oficinaPage);
            model.addAttribute("query", query);
            model.addAttribute("estado", estado);
            model.addAttribute("estadoOficina", pe.edu.unasam.activos.common.enums.EstadoOficina.values());
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());
            model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());

            // Construir queryParams para la paginación
            UriComponentsBuilder queryParamsBuilder = UriComponentsBuilder.newInstance();
            if (query != null && !query.isBlank()) {
                queryParamsBuilder.queryParam("query", query);
            }
            if (estado != null) {
                queryParamsBuilder.queryParam("estado", estado.name());
            }
            model.addAttribute("queryParams", queryParamsBuilder.build().getQuery());
            model.addAttribute("url", "/organizacion/oficinas");

            if (!model.containsAttribute("oficina")) {
                model.addAttribute("oficina", new OficinaDTO.Request());
            }

            if (!fragment) {
                return "organizacion/oficinas/index";
            } else {
                return "organizacion/oficinas/oficinas-list :: oficinaList";
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar oficinas", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('OFICINAS_CREAR')")
    public String crear(@Valid @ModelAttribute("oficina") OficinaDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.oficina", result);
            redirectAttributes.addFlashAttribute("oficina", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/organizacion/oficinas";
        }
        try {
            oficinaService.create(request);
            redirectAttributes.addFlashAttribute("success", "Oficina creada correctamente.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", "Error al crear oficina: " + ex.getMessage());
        }
        return "redirect:/organizacion/oficinas";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('OFICINAS_EDITAR')")
    public String editar(@PathVariable("id") Integer id, Model model) {
        try {
            OficinaDTO.Response oficina = oficinaService.getById(id);
            model.addAttribute("oficina", oficina);
            return "organizacion/oficinas/modal/EditarModal :: editarForm";
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (BusinessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar oficina: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('OFICINAS_EDITAR')")
    public String actualizar(@PathVariable("id") Integer id,
            @Valid @ModelAttribute("oficina") OficinaDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.oficina", result);
            redirectAttributes.addFlashAttribute("oficina", request);
            redirectAttributes.addFlashAttribute("errorOficinaId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/organizacion/oficinas";
        }
        try {
            oficinaService.update(id, request);
            redirectAttributes.addFlashAttribute("success", "Oficina actualizada correctamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorOficinaId", id);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorOficinaId", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error inesperado al actualizar la oficina: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorOficinaId", id);
        }
        return "redirect:/organizacion/oficinas";
    }

    @PostMapping("/{id}/cambiar-estado")
    @PreAuthorize("hasAuthority('OFICINAS_EDITAR')")
    public String cambiarEstado(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            oficinaService.toggleEstado(id);
            redirectAttributes.addFlashAttribute("success", "Estado de la oficina cambiado exitosamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/organizacion/oficinas";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('OFICINAS_ELIMINAR')")
    public String eliminar(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        try {
            OficinaDTO.Response resp = oficinaService.getById(id);
            oficinaService.delete(id);
            redirectAttributes.addFlashAttribute("success",
                    "Oficina '" + (resp.getNombreOficina()) + "' eliminada correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorOficinaDeleteId", id);
            redirectAttributes.addFlashAttribute("errorOficinaDeleteNombre", "ID " + id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/organizacion/oficinas";
    }
}
