package pe.edu.unasam.activos.modules.sistema.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.common.enums.EstadoRol;
import pe.edu.unasam.activos.modules.sistema.dto.RolDTO;
import pe.edu.unasam.activos.modules.sistema.service.RolService;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/seguridad/roles")
public class RolController {

    private final RolService rolService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLES_LEER')")
    public String listRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombreRol") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) EstadoRol estado,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RolDTO.Response> rolesPage = rolService.findAll(query, estado, pageable);

        model.addAttribute("rolesPage", rolesPage);
        model.addAttribute("searchQuery", query);
        model.addAttribute("estado", estado);

        return "sistema/roles/index";
    }

    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLES_LEER')")
    public ResponseEntity<RolDTO.Response> getRolById(@PathVariable Integer id) {
        return rolService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLES_CREAR')")
    public String createRol(RolDTO.Request rolRequest) {
        rolService.save(rolRequest);
        return "redirect:/seguridad/roles";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLES_EDITAR')")
    public String updateRol(@PathVariable Integer id, RolDTO.Request rolRequest) {
        rolService.update(id, rolRequest);
        return "redirect:/seguridad/roles";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLES_ELIMINAR')")
    public String deleteRol(@PathVariable Integer id) {
        rolService.deleteById(id);
        return "redirect:/seguridad/roles";
    }
}
