package pe.edu.unasam.activos.modules.ubicaciones.web;

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
import pe.edu.unasam.activos.modules.ubicaciones.dto.DepartamentoDTO;
import pe.edu.unasam.activos.modules.ubicaciones.service.DepartamentoService;

import jakarta.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/organizacion/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;
    private final PaginationProperties paginationProperties;

    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_ACCEDER')")
    public String listar(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) {

        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            Page<DepartamentoDTO.Response> departamentoPage = departamentoService.getAllDepartamentos(query,
                    normalizedPageable);

            model.addAttribute("departamentoPage", departamentoPage);
            model.addAttribute("query", query);
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());

            UriComponentsBuilder queryParamsBuilder = UriComponentsBuilder.newInstance();
            if (query != null && !query.isBlank()) {
                queryParamsBuilder.queryParam("query", query);
            }
            model.addAttribute("queryParams", queryParamsBuilder.build().getQuery());

            if (!model.containsAttribute("departamento")) {
                model.addAttribute("departamento", new DepartamentoDTO.Request());
            }
            model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());

            if (!fragment) {
                return "organizacion/departamentos/index";
            } else {
                return "organizacion/departamentos/departamentos-list :: departamentoList";
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar departamentos", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_CREAR')")
    public String crear(@Valid @ModelAttribute("departamento") DepartamentoDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.departamento", result);
            redirectAttributes.addFlashAttribute("departamento", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/organizacion/departamentos";
        }
        try {
            departamentoService.createDepartamento(request);
            redirectAttributes.addFlashAttribute("success", "Departamento creado correctamente.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", "Error al crear departamento: " + ex.getMessage());
        }
        return "redirect:/organizacion/departamentos";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_EDITAR')")
    public String editar(@PathVariable("id") Integer id, Model model) {
        try {
            var departamento = departamentoService.getDepartamentoById(id);
            model.addAttribute("departamento", departamento);
            return "organizacion/departamentos/modal/EditarModal :: editarForm";
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (BusinessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar departamento: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_EDITAR')")
    public String actualizar(@PathVariable("id") Integer id,
            @Valid @ModelAttribute("departamento") DepartamentoDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.departamento", result);
            redirectAttributes.addFlashAttribute("departamento", request);
            redirectAttributes.addFlashAttribute("errorDepartamentoId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/organizacion/departamentos";
        }
        try {
            departamentoService.updateDepartamento(id, request);
            redirectAttributes.addFlashAttribute("success", "Departamento actualizado correctamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorDepartamentoId", id);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorDepartamentoId", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error inesperado al actualizar el departamento: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorDepartamentoId", id);
        }
        return "redirect:/organizacion/departamentos";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('DEPARTAMENTOS_ELIMINAR')")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            DepartamentoDTO.Response resp = departamentoService.getDepartamentoById(id);
            departamentoService.deleteDepartamento(id);
            redirectAttributes.addFlashAttribute("success",
                    "Departamento '" + resp.getNombreDepartamento() + "' eliminado correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorDepartamentoDeleteId", id);
            redirectAttributes.addFlashAttribute("errorDepartamentoDeleteNombre", "ID " + id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/organizacion/departamentos";
    }
}
