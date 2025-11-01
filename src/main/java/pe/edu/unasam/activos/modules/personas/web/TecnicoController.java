package pe.edu.unasam.activos.modules.personas.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.unasam.activos.common.enums.EstadoTecnico;
import pe.edu.unasam.activos.common.enums.Genero;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.config.PaginationProperties;
import pe.edu.unasam.activos.modules.personas.dto.TecnicoDTO;
import pe.edu.unasam.activos.modules.personas.service.EspecialidadTecnicoService;
import pe.edu.unasam.activos.modules.personas.service.TecnicoService;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping({"/mantenimientos/tecnicos", "/organizacion/tecnicos"})
public class TecnicoController {

    private final TecnicoService tecnicoService;
    private final EspecialidadTecnicoService especialidadService;
    private final PaginationProperties paginationProperties;

    // --- Métodos de ayuda para manejo de errores ---
    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('TECNICOS_ACCEDER')")
    public String listar(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) EstadoTecnico estado,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) {
        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            Page<TecnicoDTO.Response> tecnicosPage = tecnicoService.getAllTecnicos(query, estado, normalizedPageable);
            model.addAttribute("tecnicosPage", tecnicosPage);
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

            if (!model.containsAttribute("tecnico")) {
                model.addAttribute("tecnico", new TecnicoDTO.Request());
            }
            model.addAttribute("generos", Genero.values());
            model.addAttribute("especialidades", especialidadService.getAllEspecialidadesAsList());
            // Lista de estados para el filtro del template
            model.addAttribute("estadoTecnicos", pe.edu.unasam.activos.common.enums.EstadoTecnico.values());

            if (fragment) {
                return "mantenimiento/tecnicos/tecnicos-list :: tecnicoList";
            }
            return "mantenimiento/tecnicos/index";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar técnicos", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TECNICOS_CREAR')")
    public String createTecnico(@Valid @ModelAttribute("tecnico") TecnicoDTO.Request request, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tecnico", result);
            redirectAttributes.addFlashAttribute("tecnico", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/mantenimientos/tecnicos";
        }
        try {
            tecnicoService.createTecnico(request);
            redirectAttributes.addFlashAttribute("success", "Técnico creado exitosamente.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tecnico", result);
            redirectAttributes.addFlashAttribute("tecnico", request);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/mantenimientos/tecnicos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tecnico", result);
            redirectAttributes.addFlashAttribute("tecnico", request);
            redirectAttributes.addFlashAttribute("error", "Error al crear técnico: " + e.getMessage());
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/mantenimientos/tecnicos";
        }
        return "redirect:/mantenimientos/tecnicos";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('TECNICOS_EDITAR')")
    public String getEditTecnicoForm(@PathVariable Integer id, Model model) {
        try {
            TecnicoDTO.Response tecnico = tecnicoService.getTecnicoById(id);
            model.addAttribute("tecnico", tecnico);
            model.addAttribute("generos", Genero.values());
            model.addAttribute("especialidades", especialidadService.getAllEspecialidadesAsList());
            return "mantenimiento/tecnicos/modal/EditarModal :: editarForm";
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (BusinessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar técnico: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('TECNICOS_EDITAR')")
    public String updateTecnico(@PathVariable Integer id, @Valid @ModelAttribute("tecnico") TecnicoDTO.Request request,
            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tecnico", result);
            redirectAttributes.addFlashAttribute("tecnico", request);
            redirectAttributes.addFlashAttribute("errorTecnicoId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/mantenimientos/tecnicos";
        }
        try {
            tecnicoService.updateTecnico(id, request);
            redirectAttributes.addFlashAttribute("success", "Técnico actualizado exitosamente.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tecnico", result);
            redirectAttributes.addFlashAttribute("tecnico", request);
            redirectAttributes.addFlashAttribute("errorTecnicoId", id);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/mantenimientos/tecnicos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tecnico", result);
            redirectAttributes.addFlashAttribute("tecnico", request);
            redirectAttributes.addFlashAttribute("errorTecnicoId", id);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar técnico: " + e.getMessage());
            return "redirect:/mantenimientos/tecnicos";
        }
        return "redirect:/mantenimientos/tecnicos";
    }

    @PostMapping("/{id}/cambiar-estado")
    @PreAuthorize("hasAuthority('TECNICOS_EDITAR')")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            tecnicoService.toggleStatus(id);
            redirectAttributes.addFlashAttribute("success", "Estado del técnico cambiado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/mantenimientos/tecnicos";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('TECNICOS_ELIMINAR')")
    public String deleteTecnico(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            tecnicoService.deleteTecnico(id);
            redirectAttributes.addFlashAttribute("success", "Técnico eliminado exitosamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Técnico no encontrado: " + e.getMessage());
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            try {
                TecnicoDTO.Response tecnicoResp = tecnicoService.getTecnicoById(id);
                redirectAttributes.addFlashAttribute("errorTecnicoDeleteNombre", tecnicoResp.getNombrePersona());
            } catch (Exception ignored) {
            }
            redirectAttributes.addFlashAttribute("errorTecnicoDeleteId", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar técnico: " + e.getMessage());
            try {
                TecnicoDTO.Response tecnicoResp = tecnicoService.getTecnicoById(id);
                redirectAttributes.addFlashAttribute("errorTecnicoDeleteNombre", tecnicoResp.getNombrePersona());
            } catch (Exception ignored) {
            }
            redirectAttributes.addFlashAttribute("errorTecnicoDeleteId", id);
        }
        return "redirect:/mantenimientos/tecnicos";
    }
}
