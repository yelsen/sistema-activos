package pe.edu.unasam.activos.modules.sistema.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.unasam.activos.common.enums.EstadoSesion;
import pe.edu.unasam.activos.modules.sistema.service.SesionUsuarioService;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sistema/sesiones")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class SesionUsuarioController {

    private final SesionUsuarioService sesionUsuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('SESIONES_LEER')")
    public String listSesiones(Model model,
                               @RequestParam(required = false) String q,
                               @RequestParam(required = false) EstadoSesion estado,
                               @PageableDefault(size = 10, sort = "fechaInicio") Pageable pageable) {
        
        java.util.Map<String, Object> params = new HashMap<>();
        params.put("q", q);
        if (estado != null) {
            params.put("estado", estado.name());
        }

        model.addAttribute("page", sesionUsuarioService.getSesiones(q, estado, pageable));
        model.addAttribute("params", params);
        model.addAttribute("estados", EstadoSesion.values());
        return "sistema/sesiones/list";
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
