package pe.edu.unasam.activos.audit.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.unasam.activos.audit.service.AuditoriaLogService;
import pe.edu.unasam.activos.common.enums.AccionAuditoria;
import java.util.Map;

import java.time.LocalDate;

@Controller
@RequestMapping("/sistema/auditoria")
@RequiredArgsConstructor
public class AuditoriaLogController {
    private final AuditoriaLogService auditoriaLogService;

    @GetMapping
    public String showAuditoriaPage(
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(required = false) String modulo,
            @RequestParam(required = false) AccionAuditoria accion,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @PageableDefault(size = 10, sort = "fechaHora", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
 
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("q", query);
        params.put("modulo", modulo);
        if (accion != null) {
            params.put("accion", accion.name());
        }
        if (fechaInicio != null) {
            params.put("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            params.put("fechaFin", fechaFin);
        }

        model.addAttribute("page", auditoriaLogService.getLogs(query, modulo, accion, fechaInicio, fechaFin, pageable));
        model.addAttribute("params", params);
        model.addAttribute("query", query);
        model.addAttribute("modulo", modulo);
        model.addAttribute("accion", accion);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("modulos", auditoriaLogService.getDistinctModulos());
        model.addAttribute("acciones", AccionAuditoria.values());

        return "sistema/auditoria/index";
    }
}
