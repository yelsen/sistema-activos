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
import pe.edu.unasam.activos.common.enums.EstadoModulo;
import pe.edu.unasam.activos.modules.sistema.dto.ModuloSistemaDTO;
import pe.edu.unasam.activos.modules.sistema.service.ModuloSistemaService;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/seguridad/modulos")
public class ModuloSistemaController {

    private final ModuloSistemaService moduloService;
    private final PaginationProperties paginationProperties;

    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(java.util.stream.Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MODULOS_ACCEDER')")
    public String listar(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) {
        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            // Si hay query, usar filtro por nombre
            Page<ModuloSistemaDTO.Response> moduloPage;
            if (query != null && !query.isBlank()) {
                ModuloSistemaDTO.FilterRequest filtro = ModuloSistemaDTO.FilterRequest.builder()
                        .nombreModulo(query.trim())
                        .build();
                moduloPage = moduloService.buscarConFiltros(filtro, normalizedPageable);
            } else {
                moduloPage = moduloService.listarPaginado(normalizedPageable);
            }

            model.addAttribute("moduloPage", moduloPage);
            model.addAttribute("query", query);
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());
            model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());

            if (!model.containsAttribute("modulo")) {
                ModuloSistemaDTO.Request req = new ModuloSistemaDTO.Request(null, null, null, null, null, null,
                        EstadoModulo.ACTIVO, null);
                model.addAttribute("modulo", req);
            }

            if (!fragment) {
                return "seguridad/modulos/index";
            } else {
                return "seguridad/modulos/modulos-list :: moduloList";
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar módulos", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MODULOS_CREAR')")
    public String crear(@Valid @ModelAttribute("modulo") ModuloSistemaDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.modulo", result);
            redirectAttributes.addFlashAttribute("modulo", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/seguridad/modulos";
        }
        try {
            moduloService.crear(request);
            redirectAttributes.addFlashAttribute("success", "Módulo creado correctamente.");
        } catch (Exception ex) { // Incluye validaciones de unicidad en el servicio
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", "Error al crear módulo: " + ex.getMessage());
        }
        return "redirect:/seguridad/modulos";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('MODULOS_EDITAR')")
    public String editar(@PathVariable("id") Integer id, Model model) {
        try {
            ModuloSistemaDTO.Response modulo = moduloService.buscarPorId(id);
            ModuloSistemaDTO.Request req = new ModuloSistemaDTO.Request(
                    modulo.getIdModuloSistemas(),
                    modulo.getNombreModulo(),
                    modulo.getDescripcionModulo(),
                    modulo.getIconoModulo(),
                    modulo.getRutaModulo(),
                    modulo.getOrdenModulo(),
                    modulo.getEstadoModulo(),
                    null);
            model.addAttribute("idModulo", modulo.getIdModuloSistemas());
            model.addAttribute("modulo", req);
            return "seguridad/modulos/modal/EditarModal :: editarForm";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar módulo: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('MODULOS_EDITAR')")
    public String updateModulo(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("modulo") ModuloSistemaDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.modulo", result);
            redirectAttributes.addFlashAttribute("modulo", request);
            redirectAttributes.addFlashAttribute("errorModuloId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/seguridad/modulos";
        }
        try {
            request.setIdModuloSistemas(id);
            moduloService.actualizar(request);
            redirectAttributes.addFlashAttribute("success", "Módulo actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorModuloId", id);
        }
        return "redirect:/seguridad/modulos";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('MODULOS_ELIMINAR')")
    public String eliminar(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        try {
            ModuloSistemaDTO.Response resp = moduloService.buscarPorId(id);
            moduloService.eliminar(id);
            redirectAttributes.addFlashAttribute("success",
                    "Módulo '" + resp.getNombreModulo() + "' eliminado correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorModuloDeleteId", id);
            redirectAttributes.addFlashAttribute("errorModuloDeleteNombre", "ID " + id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/seguridad/modulos";
    }
}
