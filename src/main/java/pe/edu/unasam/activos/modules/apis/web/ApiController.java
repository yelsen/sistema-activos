package pe.edu.unasam.activos.modules.apis.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.unasam.activos.modules.apis.dto.ApiRequest;
import pe.edu.unasam.activos.modules.apis.dto.ApiDto;
import pe.edu.unasam.activos.modules.apis.service.ApiService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/apis")
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    
    private final ApiService apiService;
    
    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idApi") String sortBy,
            @RequestParam(required = false) String search,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<ApiDto> apisPage = apiService.findAllPaginated(pageable);
        
        model.addAttribute("apis", apisPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", apisPage.getTotalPages());
        model.addAttribute("totalItems", apisPage.getTotalElements());
        model.addAttribute("search", search);
        
        return "apis/list";
    }
    
    @GetMapping("/nuevo")
    public String showCreateForm(Model model) {
        model.addAttribute("apiRequest", new ApiRequest());
        model.addAttribute("titulo", "Registrar Nueva API");
        return "apis/form";
    }
    
    @PostMapping
    public String create(
            @Valid @ModelAttribute("apiRequest") ApiRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Registrar Nueva API");
            return "apis/form";
        }
        
        try {
            ApiDto api = apiService.create(request);
            redirectAttributes.addFlashAttribute("success", 
                "API registrada exitosamente: " + api.getNombreApi());
            return "redirect:/apis";
        } catch (Exception e) {
            log.error("Error al crear API", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Registrar Nueva API");
            return "apis/form";
        }
    }
    
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        try {
            ApiDto api = apiService.findById(id);
            var integraciones = apiService.findIntegracionesByApi(id);
            
            model.addAttribute("api", api);
            model.addAttribute("integraciones", integraciones);
            return "apis/detail";
        } catch (Exception e) {
            log.error("Error al buscar API", e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/apis";
        }
    }
    
    @GetMapping("/{id}/editar")
    public String showEditForm(@PathVariable Integer id, Model model) {
        try {
            ApiDto api = apiService.findById(id);
            
            ApiRequest request = ApiRequest.builder()
                    .nombreApi(api.getNombreApi())
                    .descripcionApi(api.getDescripcionApi())
                    .urlApi(api.getUrlApi())
                    .versionApi(api.getVersionApi())
                    .metodoAutenticacion(api.getMetodoAutenticacion())
                    .build();
            
            model.addAttribute("apiRequest", request);
            model.addAttribute("apiId", id);
            model.addAttribute("titulo", "Editar API");
            return "apis/form";
        } catch (Exception e) {
            log.error("Error al buscar API para editar", e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/apis";
        }
    }
    
    @PostMapping("/{id}")
    public String update(
            @PathVariable Integer id,
            @Valid @ModelAttribute("apiRequest") ApiRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("apiId", id);
            model.addAttribute("titulo", "Editar API");
            return "apis/form";
        }
        
        try {
            ApiDto api = apiService.update(id, request);
            redirectAttributes.addFlashAttribute("success", 
                "API actualizada exitosamente: " + api.getNombreApi());
            return "redirect:/apis";
        } catch (Exception e) {
            log.error("Error al actualizar API", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("apiId", id);
            model.addAttribute("titulo", "Editar API");
            return "apis/form";
        }
    }
    
    @PostMapping("/{id}/eliminar")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            apiService.delete(id);
            redirectAttributes.addFlashAttribute("success", "API eliminada exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar API", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/apis";
    }
    
    // Gestión de integraciones
    @GetMapping("/{id}/integraciones")
    public String listIntegraciones(@PathVariable Integer id, Model model) {
        try {
            ApiDto api = apiService.findById(id);
            var integraciones = apiService.findIntegracionesByApi(id);
            
            model.addAttribute("api", api);
            model.addAttribute("integraciones", integraciones);
            return "apis/integraciones/list";
        } catch (Exception e) {
            log.error("Error al listar integraciones", e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/apis";
        }
    }
} 