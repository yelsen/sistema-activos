package pe.edu.unasam.activos.modules.personas.web;

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
import pe.edu.unasam.activos.modules.personas.dto.CargoDTO;
import pe.edu.unasam.activos.modules.personas.service.CargoService;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;

@Controller
@RequestMapping("/organizacion/cargos")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CargoController {
    private final CargoService cargoService;
    private final PaginationProperties paginationProperties;

    private String buildErrorMessage(BindingResult result) {
        return "Corrige los siguientes errores:\n" + result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(java.util.stream.Collectors.joining("\n"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CARGOS_ACCEDER')")
    public String listar(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) {
        try {
            int normalizedSize = paginationProperties.normalizeSize(pageable.getPageSize());
            Pageable normalizedPageable = PageRequest.of(pageable.getPageNumber(), normalizedSize);

            Page<CargoDTO.Response> cargoPage = cargoService.getAllCargos(query, normalizedPageable);

            model.addAttribute("cargoPage", cargoPage);
            model.addAttribute("query", query);
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());
            model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());

            if (!model.containsAttribute("cargo")) {
                model.addAttribute("cargo", new CargoDTO.Request());
            }

            if (!fragment) {
                return "organizacion/cargos/index";
            } else {
                return "organizacion/cargos/cargos-list :: cargoList";
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar cargos", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CARGOS_CREAR')")
    public String crear(@Valid @ModelAttribute("cargo") CargoDTO.Request request,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cargo", result);
            redirectAttributes.addFlashAttribute("cargo", request);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/organizacion/cargos";
        }
        try {
            cargoService.createCargo(request);
            redirectAttributes.addFlashAttribute("success", "Cargo creado correctamente.");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            redirectAttributes.addFlashAttribute("error", "Error al crear cargo: " + ex.getMessage());
        }
        return "redirect:/organizacion/cargos";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('CARGOS_EDITAR')")
    public String editar(@PathVariable("id") Integer id, Model model) {
        try {
            CargoDTO.Response cargo = cargoService.getCargoById(id);
            model.addAttribute("idCargo", cargo.getIdCargo());
            model.addAttribute("cargo", CargoDTO.Request.builder().nombreCargo(cargo.getNombreCargo()).build());
            return "organizacion/cargos/modal/EditarModal :: editarForm";
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al cargar cargo: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('CARGOS_EDITAR')")
    public String updateCargo(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("cargo") CargoDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cargo", result);
            redirectAttributes.addFlashAttribute("cargo", request);
            redirectAttributes.addFlashAttribute("errorCargoId", id);
            redirectAttributes.addFlashAttribute("error", buildErrorMessage(result));
            return "redirect:/organizacion/cargos";
        }
        try {
            cargoService.updateCargo(id, request);
            redirectAttributes.addFlashAttribute("success", "Cargo actualizado correctamente.");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorCargoId", id);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("errorCargoId", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error inesperado al actualizar el cargo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorCargoId", id);
        }
        return "redirect:/organizacion/cargos";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('CARGOS_ELIMINAR')")
    public String eliminar(@PathVariable("id") Integer id,
                           RedirectAttributes redirectAttributes) {
        try {
            CargoDTO.Response resp = cargoService.getCargoById(id);
            cargoService.deleteCargo(id);
            redirectAttributes.addFlashAttribute("success",
                    "Cargo '" + resp.getNombreCargo() + "' eliminado correctamente.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorCargoDeleteId", id);
            redirectAttributes.addFlashAttribute("errorCargoDeleteNombre", "ID " + id);
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/organizacion/cargos";
    }
}
