package pe.edu.unasam.activos.modules.sistema.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.unasam.activos.common.enums.EstadoSesion;
import pe.edu.unasam.activos.modules.sistema.service.SesionUsuarioService;
import pe.edu.unasam.activos.config.PaginationProperties;

import java.util.List;

@Controller
@RequestMapping("/sistema/sesiones")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class SesionUsuarioController {

    private final SesionUsuarioService sesionUsuarioService;
    private final PaginationProperties paginationProperties;

    @GetMapping
    @PreAuthorize("hasAuthority('SESIONES_ACCEDER')")
    public String listSesiones(Model model,
            @RequestParam(required = false, name = "query") String query,
            @RequestParam(required = false, name = "estado") String estadoParam,
            Pageable pageable) {

        EstadoSesion estadoFiltro = null;
        if (StringUtils.hasText(estadoParam) && !"todos".equalsIgnoreCase(estadoParam)) {
            try {
                estadoFiltro = EstadoSesion.valueOf(estadoParam);
            } catch (IllegalArgumentException ex) {
                // Si llega un valor inválido, no filtrar por estado
                estadoFiltro = null;
            }
        }

        model.addAttribute("page", sesionUsuarioService.getSesiones(query, estadoFiltro, pageable));
        model.addAttribute("query", query);
        model.addAttribute("estado", StringUtils.hasText(estadoParam) ? estadoParam : "todos");
        model.addAttribute("estados", EstadoSesion.values());
        model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());
        return "sistema/sesiones/index";
    }

    @PostMapping("/cerrar/{id}")
    @PreAuthorize("hasAuthority('SESIONES_ELIMINAR')")
    public String closeSession(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        sesionUsuarioService.closeSession(id);
        redirectAttributes.addFlashAttribute("message", "Sesión cerrada exitosamente.");
        return "redirect:/sistema/sesiones";
    }

    @PostMapping("/cerrar-masivo")
    @PreAuthorize("hasAuthority('SESIONES_ELIMINAR')")
    public String closeMultipleSessions(@RequestParam("ids") List<Integer> ids, RedirectAttributes redirectAttributes) {
        sesionUsuarioService.closeMultipleSessions(ids);
        redirectAttributes.addFlashAttribute("message", "Las sesiones seleccionadas han sido cerradas.");
        return "redirect:/sistema/sesiones?success=true";
    }
}
