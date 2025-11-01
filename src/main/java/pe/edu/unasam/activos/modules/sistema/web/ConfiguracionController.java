package pe.edu.unasam.activos.modules.sistema.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.unasam.activos.modules.sistema.dto.ConfiguracionDTO;
import pe.edu.unasam.activos.modules.sistema.service.ConfiguracionService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sistema/configuraciones")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    @GetMapping
    @PreAuthorize("hasAuthority('CONFIGURACION_ACCEDER')")
    public String showConfiguracionPage(Model model) {
        // Obtenemos todas las configuraciones y las agrupamos por categoría
        List<ConfiguracionDTO.Response> allConfigs = configuracionService.getAllAsList();
        Map<String, List<ConfiguracionDTO.Response>> configsByCategory = allConfigs.stream()
                .collect(Collectors.groupingBy(
                        config -> config.getCategoriaConfig() != null ? config.getCategoriaConfig() : "GENERAL",
                        Collectors.toList()));

        model.addAttribute("configsByCategory", configsByCategory);
        return "sistema/configuraciones/index";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('CONFIGURACION_EDITAR')")
    public String saveConfiguracion(ConfiguracionDTO.BulkUpdateRequest request, RedirectAttributes redirectAttributes) {
        configuracionService.updateFromMap(request.configs());
        redirectAttributes.addFlashAttribute("success", "Configuración guardada exitosamente.");
        return "redirect:/sistema/configuraciones";
    }
}
