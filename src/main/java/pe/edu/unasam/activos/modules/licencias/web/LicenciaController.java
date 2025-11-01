package pe.edu.unasam.activos.modules.licencias.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/licencias")
public class LicenciaController {

    @GetMapping
    @PreAuthorize("hasAuthority('LICENCIAS_ACCEDER')")
    public String listarLicencias() {
        return "licencias/list";
    }

    @GetMapping("/tipos")
    @PreAuthorize("hasAuthority('TIPO_LICENCIAS_ACCEDER')")
    public String listarTiposLicencias() {
        return "licencias/tipos/list";
    }

    @GetMapping("/asignaciones")
    @PreAuthorize("hasAuthority('ASIGNACIONES_ACCEDER')")
    public String listarAsignacionesLicencias() {
        return "licencias/asignaciones/list";
    }
}
