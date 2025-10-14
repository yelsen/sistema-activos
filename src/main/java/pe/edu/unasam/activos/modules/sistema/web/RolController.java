package pe.edu.unasam.activos.modules.sistema.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.common.enums.EstadoRol;
import pe.edu.unasam.activos.modules.sistema.dto.RolDTO;
import pe.edu.unasam.activos.modules.sistema.service.PermisoService;
import pe.edu.unasam.activos.modules.sistema.service.RolService;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("isAuthenticated()")
@RequestMapping("/seguridad/roles")
public class RolController {

    private final RolService rolService;
    private final PermisoService permisoService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLES_ACCEDER')")
    public String listRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombreRol") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) EstadoRol estado,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RolDTO.Response> rolesPage = rolService.findAll(query, estado, pageable, "ROLES_LEER");

        model.addAttribute("rolesPage", rolesPage);
        model.addAttribute("searchQuery", query);
        model.addAttribute("estado", estado);
        model.addAttribute("permisosTabla", permisoService.getPermisosAsTabla());
        model.addAttribute("acciones", permisoService.getAllAcciones());

        return "sistema/roles/index";
    }

    @GetMapping("/filtrar")
    @PreAuthorize("hasAuthority('ROLES_LEER')")
    public String filterRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombreRol") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) EstadoRol estado,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RolDTO.Response> rolesPage = rolService.findAll(query, estado, pageable, "ROLES_LEER");

        model.addAttribute("rolesPage", rolesPage);

        return "sistema/roles/roles-content :: rolesList";
    }

    @GetMapping("/{id}/json")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLES_VER')")
    public ResponseEntity<RolDTO.Response> getRolDetailsJson(@PathVariable Integer id) {
        return rolService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('ROLES_EDITAR')")
    public String showEditForm(@PathVariable Integer id, Model model) {
        RolDTO.Response rol = rolService.findById(id).orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        model.addAttribute("permisosTabla", permisoService.getPermisosAsTabla());
        model.addAttribute("acciones", permisoService.getAllAcciones());
        model.addAttribute("rol", rol);
        model.addAttribute("permisosSeleccionados",
                rol.getPermisos() != null ? rol.getPermisos().stream()
                        .filter(p -> p.isPermitido() && p.getPermiso() != null
                                && p.getPermiso().getModuloSistema() != null && p.getPermiso().getAccion() != null)
                        .map(p -> p.getPermiso().getModuloSistema().getIdModuloSistemas() + "-"
                                + p.getPermiso().getAccion().getIdAccion())
                        .collect(Collectors.toSet())
                        : java.util.Collections.emptySet());

        return "sistema/roles/modal/EditarModal :: contenido-editar";
    }

    @GetMapping("/detalles/{id}")
    @PreAuthorize("hasAuthority('ROLES_VER')")
    public String showDetails(@PathVariable Integer id, Model model) {
        RolDTO.Response rol = rolService.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Obtenemos la estructura completa de permisos y los IDs de los permisos del
        // rol
        model.addAttribute("permisosTabla", permisoService.getPermisosAsTabla());
        model.addAttribute("acciones", permisoService.getAllAcciones());
        model.addAttribute("rol", rol);
        model.addAttribute("permisosSeleccionados",
                rol.getPermisos() != null ? rol.getPermisos().stream()
                        .filter(p -> p.isPermitido() && p.getPermiso() != null
                                && p.getPermiso().getModuloSistema() != null && p.getPermiso().getAccion() != null)
                        .map(p -> p.getPermiso().getModuloSistema().getIdModuloSistemas() + "-"
                                + p.getPermiso().getAccion().getIdAccion())
                        .collect(Collectors.toSet())
                        : java.util.Collections.emptySet());
        return "sistema/roles/modal/DetalleModal :: detalle-content";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLES_CREAR')")
    public String createRol(RolDTO.Request rolRequest, RedirectAttributes redirectAttributes) {
        log.info("Recibida solicitud para crear rol. Datos: {}", rolRequest);
        RolDTO.Response nuevoRol = rolService.save(rolRequest);
        redirectAttributes.addFlashAttribute("success", "Rol '" + nuevoRol.getNombreRol() + "' creado exitosamente.");
        return "redirect:/seguridad/roles";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLES_EDITAR')")
    public String updateRol(@PathVariable Integer id, RolDTO.Request rolRequest,
            RedirectAttributes redirectAttributes) {
        RolDTO.Response rolActualizado = rolService.update(id, rolRequest);
        redirectAttributes.addFlashAttribute("success",
                "Rol '" + rolActualizado.getNombreRol() + "' actualizado correctamente.");
        return "redirect:/seguridad/roles";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLES_ELIMINAR')")
    public String deleteRol(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        rolService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "El rol ha sido eliminado (borrado lógico).");
        return "redirect:/seguridad/roles";
    }

    @PostMapping("/{id}/cambiar-estado")
    @PreAuthorize("hasAuthority('ROLES_EDITAR')")
    public String toggleRolStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        rolService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("success", "El estado del rol ha sido cambiado.");
        return "redirect:/seguridad/roles";
    }

}
