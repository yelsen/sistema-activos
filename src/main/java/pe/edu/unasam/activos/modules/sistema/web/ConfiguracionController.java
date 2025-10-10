package pe.edu.unasam.activos.modules.sistema.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.modules.sistema.dto.ConfiguracionDTO;
import pe.edu.unasam.activos.modules.sistema.service.ConfiguracionService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sistema/configuracion")
@PreAuthorize("hasAuthority('CONFIGURACION_ACCEDER')")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    @GetMapping
    @PreAuthorize("hasAuthority('CONFIGURACION_LEER')")
    public String listarConfiguraciones(Model model) {

        Map<String, List<ConfiguracionDTO.Response>> configsPorCategoria = configuracionService.getAll().stream()
                .collect(Collectors.groupingBy(ConfiguracionDTO.Response::getCategoriaConfig));

        model.addAttribute("configsPorCategoria", configsPorCategoria);
        return "sistema/configuracion/list";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('CONFIGURACION_EDITAR')")
    public String guardarConfiguraciones(@RequestParam Map<String, String> allParams) {

        configuracionService.updateFromMap(allParams);
        return "redirect:/sistema/configuracion?success=true";
    }
}
