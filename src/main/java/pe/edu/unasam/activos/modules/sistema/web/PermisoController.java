package pe.edu.unasam.activos.modules.sistema.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.unasam.activos.modules.sistema.service.PermisoService;

@Controller
@RequestMapping("/seguridad/permisos")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class PermisoController {

    private final PermisoService permisoService;

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISOS_ACCEDER')")
    public String listPermisos(Model model) {
        var permisosAgrupados = permisoService.getAllPermisosGroupedByModulo();

        model.addAttribute("permisosAgrupados", permisosAgrupados);
        return "seguridad/permisos/index";
    }
}
