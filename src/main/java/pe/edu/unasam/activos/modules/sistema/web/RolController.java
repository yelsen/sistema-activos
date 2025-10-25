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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.common.enums.EstadoRol;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.common.exception.ReferentialIntegrityException;
import pe.edu.unasam.activos.modules.sistema.dto.RolDTO;
import pe.edu.unasam.activos.modules.sistema.service.PermisoService;
import pe.edu.unasam.activos.modules.sistema.service.RolService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Set;
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
                        @RequestParam(defaultValue = "9") int size,
                        @RequestParam(defaultValue = "nombreRol") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir,
                        @RequestParam(required = false) String query,
                        @RequestParam(required = false) EstadoRol estado,
                        Model model,
                        HttpServletRequest request) {

                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();
                Pageable pageable = PageRequest.of(page, size, sort);
                Page<RolDTO.Response> rolesPage = rolService.findAll(query, estado, pageable, "ROLES_LEER");
                model.addAttribute("rolesPage", rolesPage);
                model.addAttribute("searchQuery", query);
                model.addAttribute("estado", estado);
                model.addAttribute("permisosTabla", permisoService.getPermisosAsTabla());
                model.addAttribute("acciones", permisoService.getAllAcciones());
                if ("true".equals(request.getHeader("X-Requested-With-Fragment"))) {
                        return "seguridad/roles/roles-content :: rolesList";
                }
                return "seguridad/roles/index";
        }

        @GetMapping("/{id}/json")
        @ResponseBody
        @PreAuthorize("hasAuthority('ROLES_VER')")
        public ResponseEntity<RolDTO.Response> getRolDetailsJson(@PathVariable Integer id) {
                return rolService.findById(id).map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @GetMapping("/editar/{id}")
        @PreAuthorize("hasAuthority('ROLES_EDITAR')")
        public String showEditForm(@PathVariable Integer id, Model model) {
                RolDTO.Response rol = rolService.findById(id)
                                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));
                model.addAttribute("permisosTabla", permisoService.getPermisosAsTabla());
                model.addAttribute("acciones", permisoService.getAllAcciones());
                model.addAttribute("rol", rol);
                model.addAttribute("permisosSeleccionados", getPermisosSeleccionados(rol));
                return "seguridad/roles/modal/EditarModal :: contenido-editar";
        }

        @GetMapping("/detalles/{id}")
        @PreAuthorize("hasAuthority('ROLES_VER')")
        public String showDetails(@PathVariable Integer id, Model model) {
                RolDTO.Response rol = rolService.findById(id)
                                .orElseThrow(() -> new NotFoundException("Rol no encontrado con ID: " + id));
                model.addAttribute("permisosTabla", permisoService.getPermisosAsTabla());
                model.addAttribute("acciones", permisoService.getAllAcciones());
                model.addAttribute("rol", rol);
                model.addAttribute("permisosSeleccionados", getPermisosSeleccionados(rol));
                return "seguridad/roles/modal/DetalleModal :: detalle-content";
        }

        @PostMapping
        @PreAuthorize("hasAuthority('ROLES_CREAR')")
        public String createRol(@Valid @ModelAttribute("rolRequest") RolDTO.Request rolRequest,
                        BindingResult bindingResult, RedirectAttributes redirectAttributes) {
                try {
                        if (bindingResult.hasErrors()) {
                                String errorMessage = bindingResult.getFieldErrors().stream()
                                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                                .collect(Collectors.joining("; "));
                                redirectAttributes.addFlashAttribute("error", "Error de validación: " + errorMessage);
                                redirectAttributes.addFlashAttribute("rolRequest", rolRequest);
                                return "redirect:/seguridad/roles";
                        }

                        RolDTO.Response nuevoRol = rolService.save(rolRequest);
                        redirectAttributes.addFlashAttribute("success",
                                        "Rol '" + nuevoRol.getNombreRol() + "' creado con éxito.");
                        return "redirect:/seguridad/roles";
                } catch (BusinessException e) {
                        redirectAttributes.addFlashAttribute("error", e.getMessage());
                        redirectAttributes.addFlashAttribute("rolRequest", rolRequest);
                        return "redirect:/seguridad/roles";
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("error",
                                        "Error inesperado al crear el rol. Por favor, intente nuevamente.");
                        redirectAttributes.addFlashAttribute("rolRequest", rolRequest);
                        return "redirect:/seguridad/roles";
                }
        }

        @PostMapping("/update/{id}")
        @PreAuthorize("hasAuthority('ROLES_EDITAR')")
        public String updateRol(@PathVariable Integer id,
                        @Valid @ModelAttribute("rolRequest") RolDTO.Request rolRequest, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
                try {
                        if (bindingResult.hasErrors()) {
                                String errorMessage = bindingResult.getFieldErrors().stream()
                                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                                .collect(Collectors.joining("; "));
                                redirectAttributes.addFlashAttribute("error", "Error de validación: " + errorMessage);
                                redirectAttributes.addFlashAttribute("rolRequest", rolRequest);
                                redirectAttributes.addFlashAttribute("errorRolId", id);
                                return "redirect:/seguridad/roles";
                        }

                        RolDTO.Response rolActualizado = rolService.update(id, rolRequest);
                        redirectAttributes.addFlashAttribute("success",
                                        "Rol '" + rolActualizado.getNombreRol() + "' actualizado correctamente.");
                        return "redirect:/seguridad/roles";
                } catch (NotFoundException e) {
                        redirectAttributes.addFlashAttribute("error", e.getMessage());
                        return "redirect:/seguridad/roles";
                } catch (BusinessException e) {
                        redirectAttributes.addFlashAttribute("error", e.getMessage());
                        redirectAttributes.addFlashAttribute("rolRequest", rolRequest);
                        redirectAttributes.addFlashAttribute("errorRolId", id);
                        return "redirect:/seguridad/roles";
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("error",
                                        "Error inesperado al actualizar el rol. Por favor, intente nuevamente.");
                        return "redirect:/seguridad/roles";
                }
        }

        @PostMapping("/{id}/cambiar-estado")
        @PreAuthorize("hasAuthority('ROLES_EDITAR')")
        public String toggleRolStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
                try {
                        rolService.toggleStatus(id);
                        redirectAttributes.addFlashAttribute("success",
                                        "El estado del rol ha sido cambiado exitosamente.");
                        return "redirect:/seguridad/roles";
                } catch (NotFoundException e) {
                        redirectAttributes.addFlashAttribute("error", e.getMessage());
                        return "redirect:/seguridad/roles";
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("error",
                                        "Error al cambiar el estado del rol. Por favor, intente nuevamente.");
                        return "redirect:/seguridad/roles";
                }
        }

        @PostMapping("/delete/{id}")
        @PreAuthorize("hasAuthority('ROLES_ELIMINAR')")
        public String deleteRol(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
                try {
                        rolService.deleteById(id);
                        redirectAttributes.addFlashAttribute("success", "El rol ha sido eliminado correctamente.");
                        return "redirect:/seguridad/roles";
                } catch (NotFoundException e) {
                        redirectAttributes.addFlashAttribute("error", e.getMessage());
                        return "redirect:/seguridad/roles";
                } catch (ReferentialIntegrityException e) {
                        redirectAttributes.addFlashAttribute("warning", e.getMessage());
                        return "redirect:/seguridad/roles";
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("error",
                                        "Error al eliminar el rol. Por favor, intente nuevamente.");
                        return "redirect:/seguridad/roles";
                }
        }

        private Set<String> getPermisosSeleccionados(RolDTO.Response rol) {
                if (rol == null || rol.getPermisos() == null) {
                        return Collections.emptySet();
                }
                return rol.getPermisos().stream()
                                .filter(p -> p.isPermitido() && p.getPermiso() != null
                                                && p.getPermiso().getModuloSistema() != null
                                                && p.getPermiso().getAccion() != null)
                                .map(p -> p.getPermiso().getModuloSistema().getIdModuloSistemas() + "-"
                                                + p.getPermiso().getAccion().getIdAccion())
                                .collect(Collectors.toSet());
        }
}