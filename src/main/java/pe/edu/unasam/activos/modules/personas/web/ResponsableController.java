package pe.edu.unasam.activos.modules.personas.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/organizacion/responsables")
public class ResponsableController {

    @GetMapping("/responsables")
    @PreAuthorize("hasAuthority('RESPONSABLES_ACCEDER')")
    public String listResponsables() {
        return "organizacion/responsables/index";
    }
    
}
