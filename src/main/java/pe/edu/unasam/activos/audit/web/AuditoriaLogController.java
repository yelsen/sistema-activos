package pe.edu.unasam.activos.audit.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.unasam.activos.audit.service.AuditoriaLogService;
import pe.edu.unasam.activos.common.enums.AccionAuditoria;
import pe.edu.unasam.activos.config.PaginationProperties;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/sistema/auditoria")
@RequiredArgsConstructor
public class AuditoriaLogController {
    private final AuditoriaLogService auditoriaLogService;
    private final PaginationProperties paginationProperties;

    @GetMapping
    public String showAuditoriaPage(
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(required = false) String modulo,
            @RequestParam(required = false) AccionAuditoria accion,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            Pageable pageable,
            Model model) {

        Map<String, Object> queryParams = new java.util.HashMap<>();
        queryParams.put("q", query);
        queryParams.put("modulo", modulo);
        if (accion != null) {
            queryParams.put("accion", accion.name());
        }
        if (fechaInicio != null) {
            queryParams.put("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            queryParams.put("fechaFin", fechaFin);
        }

        Pageable normalized = PageRequest.of(
                pageable.getPageNumber(),
                paginationProperties.normalizeSize(pageable.getPageSize()),
                pageable.getSort());

        model.addAttribute("page", auditoriaLogService.getLogs(query, modulo, accion, fechaInicio, fechaFin, normalized));
        model.addAttribute("queryParams", queryParams);
        model.addAttribute("query", query);
        model.addAttribute("modulo", modulo);
        model.addAttribute("accion", accion);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("modulos", auditoriaLogService.getDistinctModulos());
        model.addAttribute("acciones", AccionAuditoria.values());
        model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());

        return "sistema/auditoria/index";
    }
}
