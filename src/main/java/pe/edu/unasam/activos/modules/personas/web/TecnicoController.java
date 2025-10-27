package pe.edu.unasam.activos.modules.personas.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/organizacion/responsables")
public class TecnicoController {
    
    @GetMapping("/tecnicos")
    @PreAuthorize("hasAuthority('TECNICOS_ACCEDER')")
    public String listTecnicos() {
        return "organizacion/tecnicos/index";
    }
    
}
