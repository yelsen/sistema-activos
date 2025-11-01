package pe.edu.unasam.activos.modules.aplicativos.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/aplicativos")
public class AplicativoController {

    @GetMapping
    @PreAuthorize("hasAuthority('APLICATIVOS_ACCEDER')")
    public String listarAplicativos() {
        return "aplicativos/list";
    }

    @GetMapping("/tipos")
    @PreAuthorize("hasAuthority('TIPO_APLICATIVOS_ACCEDER')")
    public String listarTiposAplicativos() {
        return "aplicativos/tipos/list";
    }

    @GetMapping("/instalaciones")
    @PreAuthorize("hasAuthority('APLICATIVOS_ACCEDER')")
    public String listarInstalaciones() {
        return "aplicativos/instalaciones/list";
    }

    @GetMapping("/versiones")
    @PreAuthorize("hasAuthority('APLICATIVOS_ACCEDER')")
    public String listarVersiones() {
        return "aplicativos/versiones";
    }
}
