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
import org.springframework.web.util.UriComponentsBuilder;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.config.PaginationProperties;
import pe.edu.unasam.activos.modules.ubicaciones.dto.TipoOficinaDTO;
import pe.edu.unasam.activos.modules.ubicaciones.service.TipoOficinaService;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/organizacion/tipo-oficinas")
public class TipoOficinaController {

    private final TipoOficinaService tipoOficinaService;
    private final PaginationProperties paginationProperties;

    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('TIPO_OFICINAS_ACCEDER')")
    public String listar(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) {

        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            Page<TipoOficinaDTO.Response> tipoOficinaPage = tipoOficinaService.getAllTipoOficinas(query,
                    normalizedPageable);

            model.addAttribute("tipoOficinaPage", tipoOficinaPage);
            model.addAttribute("query", query);
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());

            UriComponentsBuilder queryParamsBuilder = UriComponentsBuilder.newInstance();
            if (query != null && !query.isBlank()) {
                queryParamsBuilder.queryParam("query", query);
            }
            model.addAttribute("queryParams", queryParamsBuilder.build().getQuery());

            if (!model.containsAttribute("tipoOficina")) {
                model.addAttribute("tipoOficina", new TipoOficinaDTO.Request());
            }
            model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());

            if (!fragment) {
                return "organizacion/tipo_oficinas/index";
            } else {
                return "organizacion/tipo_oficinas/tipo_oficinas-list :: tipoOficinaList";
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar tipos de oficina", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TIPO_OFICINAS_CREAR')")
    public String crear(@Valid @ModelAttribute("tipoOficina") TipoOficinaDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tipoOficina", result);
            redirectAttributes.addFlashAttribute("tipoOficina", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/organizacion/tipo-oficinas";
        }
        try {
            tipoOficinaService.create(request);
            redirectAttributes.addFlashAttribute("success", "Tipo de oficina creado correctamente.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", "Error al crear tipo de oficina: " + ex.getMessage());
        }
        return "redirect:/organizacion/tipo-oficinas";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('TIPO_OFICINAS_EDITAR')")
    public String editar(@PathVariable("id") Integer id, Model model) {
        try {
            TipoOficinaDTO.Response tipo = tipoOficinaService.getById(id);
            model.addAttribute("tipoOficina", tipo);
            return "organizacion/tipo_oficinas/modal/EditarModal :: editarForm";
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (BusinessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar tipo de oficina: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('TIPO_OFICINAS_EDITAR')")
    public String actualizar(@PathVariable("id") Integer id,
            @Valid @ModelAttribute("tipoOficina") TipoOficinaDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tipoOficina", result);
            redirectAttributes.addFlashAttribute("tipoOficina", request);
            redirectAttributes.addFlashAttribute("errorTipoOficinaId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/organizacion/tipo-oficinas";
        }
        try {
            tipoOficinaService.update(id, request);
            redirectAttributes.addFlashAttribute("success", "Tipo de oficina actualizado correctamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorTipoOficinaId", id);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorTipoOficinaId", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error inesperado al actualizar el tipo de oficina: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorTipoOficinaId", id);
        }
        return "redirect:/organizacion/tipo-oficinas";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('TIPO_OFICINAS_ELIMINAR')")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            TipoOficinaDTO.Response resp = tipoOficinaService.getById(id);
            tipoOficinaService.delete(id);
            redirectAttributes.addFlashAttribute("success",
                    "Tipo de oficina '" + resp.getTipoOficina() + "' eliminado correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorTipoOficinaDeleteId", id);
            redirectAttributes.addFlashAttribute("errorTipoOficinaDeleteNombre", "ID " + id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/organizacion/tipo-oficinas";
    }
}
