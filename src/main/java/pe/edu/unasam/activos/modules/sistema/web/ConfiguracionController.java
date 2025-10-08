package pe.edu.unasam.activos.modules.sistema.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.modules.sistema.domain.ConfiguracionSistema;
import pe.edu.unasam.activos.modules.sistema.service.ConfiguracionSistemaService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sistema/configuracion")
@PreAuthorize("hasAuthority('CONFIGURACION_ACCEDER')")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionSistemaService configuracionService;

    @GetMapping
    @PreAuthorize("hasAuthority('CONFIGURACION_LEER')")
    public String listarConfiguraciones(Model model) {
        // Agrupar configuraciones por categoría para la vista de tabs
        // Se obtiene la data una sola vez y se procesa.
        Map<String, List<ConfiguracionSistema>> configsPorCategoria = configuracionService.findAll().stream()
                .collect(Collectors.groupingBy(ConfiguracionSistema::getCategoriaConfig));

        model.addAttribute("configsPorCategoria", configsPorCategoria);
        return "sistema/configuracion/list";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('CONFIGURACION_EDITAR')")
    public String guardarConfiguraciones(@RequestParam Map<String, String> allParams) {
        configuracionService.updateFromMap(allParams);
        return "redirect:/sistema/configuracion";
    }

    /*
     * NOTA: Los métodos para crear, editar y eliminar configuraciones individuales
     * se pueden manejar a través de una API REST y JavaScript para una mejor UX,
     * en lugar de recargar toda la página.
     * Por ahora, el enfoque principal es editar los valores existentes.
     *
     * Ejemplo de cómo se podría manejar la edición individual si fuera necesario:
     *
     * @PostMapping("/guardar")
     * @PreAuthorize("hasAnyAuthority('CONFIGURACION_CREAR', 'CONFIGURACION_EDITAR')")
     * public String guardarConfiguracion(ConfiguracionSistema configuracion) {
     *     configuracionService.save(configuracion);
     *     return "redirect:/sistema/configuracion";
     * }
     */
}
