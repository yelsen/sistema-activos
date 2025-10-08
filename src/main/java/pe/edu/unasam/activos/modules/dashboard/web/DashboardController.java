package pe.edu.unasam.activos.modules.dashboard.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    /**
     * Muestra la página del dashboard.
     * Esta es la página a la que se redirige después de un inicio de sesión exitoso.
     * @return El nombre de la plantilla Thymeleaf "dashboard/index".
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "sistema/dashboard";
    }
}