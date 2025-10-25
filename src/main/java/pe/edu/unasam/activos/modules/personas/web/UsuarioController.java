package pe.edu.unasam.activos.modules.personas.web;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import pe.edu.unasam.activos.common.enums.Genero;
import pe.edu.unasam.activos.modules.personas.dto.UsuarioDTO;
import pe.edu.unasam.activos.modules.personas.service.UsuarioService;
import pe.edu.unasam.activos.modules.sistema.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.common.exception.BusinessException;
import pe.edu.unasam.activos.config.PaginationProperties;

import java.beans.PropertyEditorSupport;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/seguridad/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final ObjectMapper objectMapper;
    private final PaginationProperties paginationProperties;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(EstadoUsuario.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(EstadoUsuario.valueOf(text.toUpperCase()));
                }
            }
        });
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USUARIOS_ACCEDER')")
    public String listUsuarios(
            Model model,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) EstadoUsuario estado,
            @RequestParam(required = false, defaultValue = "false") boolean fragment,
            Pageable pageable) throws JsonProcessingException {

        try {
            Page<UsuarioDTO.Response> usuariosPage = usuarioService.getAllUsuarios(query, estado, pageable);
            model.addAttribute("usuariosPage", usuariosPage);
            model.addAttribute("query", query);
            model.addAttribute("estado", estado);
            model.addAttribute("pageSizes", paginationProperties.getAllowedSizes());

            // Construir queryParams para paginación
            UriComponentsBuilder queryParamsBuilder = UriComponentsBuilder.newInstance();
            if (query != null && !query.isBlank()) {
                queryParamsBuilder.queryParam("query", query);
            }
            if (estado != null) {
                queryParamsBuilder.queryParam("estado", estado.name());
            }
            model.addAttribute("queryParams", queryParamsBuilder.build().getQuery());

            var roles = rolService.findAllRolesForSelect();
            Map<Integer, String> rolColorMap = roles.stream()
                    .collect(Collectors.toMap(
                            rol -> rol.getIdRol(), // Key
                            rol -> rol.getColorRol() != null ? rol.getColorRol() : "#6c757d" // Value, con fallback
                    ));
            model.addAttribute("rolColorMap", rolColorMap);
            
            if (!fragment) {
                model.addAttribute("roles", roles);
                model.addAttribute("estadosUsuario", EstadoUsuario.values());
                model.addAttribute("generos", Genero.values());
                model.addAttribute("rolColorMapJson", objectMapper.writeValueAsString(rolColorMap));
                model.addAttribute("defaultPageSize", paginationProperties.getDefaultSize());

                if (!model.containsAttribute("usuario")) {
                    model.addAttribute("usuario", new UsuarioDTO.Request());
                }
                return "seguridad/usuarios/index";
            } else {
                return "seguridad/usuarios/usuarios-list :: usuariosList";
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cargar usuarios: " + e.getMessage(), e);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USUARIOS_CREAR')")
    public String createUsuario(
            @Valid @ModelAttribute("usuario") UsuarioDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return handleCreateError(request, result, redirectAttributes, buildErrorMessage(result));
        }

        try {
            usuarioService.createUsuario(request);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
        } catch (BusinessException e) {
            return handleCreateError(request, result, redirectAttributes, e.getMessage());
        } catch (Exception e) {
            return handleCreateError(request, result, redirectAttributes, "Error al crear usuario: " + e.getMessage());
        }

        return "redirect:/seguridad/usuarios";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_EDITAR')")
    public String getEditUsuarioForm(@PathVariable Integer id, Model model) {
        try {
            UsuarioDTO.Response usuario = usuarioService.getUsuarioById(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", rolService.findAllRolesForSelect());
            model.addAttribute("generos", Genero.values());
            return "seguridad/usuarios/modal/EditarModal :: editarForm";
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (BusinessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cargar usuario: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_EDITAR')")
    public String updateUsuario(
            @PathVariable Integer id,
            @Valid @ModelAttribute("usuario") UsuarioDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return handleEditError(id, request, result, redirectAttributes, buildErrorMessage(result));
        }

        try {
            usuarioService.updateUsuario(id, request);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
        } catch (BusinessException e) {
            return handleEditError(id, request, result, redirectAttributes, e.getMessage());
        } catch (Exception e) {
            return handleEditError(id, request, result, redirectAttributes, "Error al actualizar usuario: " + e.getMessage());
        }

        return "redirect:/seguridad/usuarios";
    }

    @PostMapping("/{id}/cambiar-estado")
    @PreAuthorize("hasAuthority('USUARIOS_EDITAR')")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.toggleStatus(id);
            redirectAttributes.addFlashAttribute("success", "Estado del usuario cambiado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/seguridad/usuarios";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_ELIMINAR')")
    public String deleteUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteUsuarioSafely(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado: " + e.getMessage());
        } catch (BusinessException e) {
            addDeleteErrorFlash(id, redirectAttributes, e.getMessage());
        } catch (Exception e) {
            addDeleteErrorFlash(id, redirectAttributes, "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/seguridad/usuarios";
    }
    
    private String getFriendlyFieldName(String field) {
        return switch (field) {
            case "usuario" -> "Usuario";
            case "contrasena" -> "Contraseña";
            case "idRol" -> "Rol";
            case "dniPersona" -> "DNI";
            case "nombres" -> "Nombres";
            case "apellidos" -> "Apellidos";
            case "email" -> "Email";
            case "telefono" -> "Teléfono";
            case "direccion" -> "Dirección";
            case "genero" -> "Género";
            case "estadoUsuarios" -> "Estado";
            default -> field;
        };
    }

    private String buildErrorMessage(BindingResult result) {
        String fieldErrors = result.getFieldErrors().stream()
                .map(fe -> "• " + getFriendlyFieldName(fe.getField()) + ": " + fe.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("\n"));
        String globalErrors = result.getGlobalErrors().stream()
                .map(ge -> "• " + ge.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("\n"));
        if (fieldErrors.isBlank() && globalErrors.isBlank()) {
            return "Se encontraron errores de validación. Por favor, revisa el formulario.";
        }
        String header = "Corrige los siguientes errores:";
        if (!globalErrors.isBlank())
            return header + "\n" + (fieldErrors.isBlank() ? "" : fieldErrors + "\n") + globalErrors;
        else
            return header + "\n" + fieldErrors;
    }

    private String handleCreateError(UsuarioDTO.Request request, BindingResult result,
                                     RedirectAttributes redirectAttributes, String errorMsg) {
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuario", result);
        redirectAttributes.addFlashAttribute("usuario", request);
        redirectAttributes.addFlashAttribute("error", errorMsg);
        redirectAttributes.addFlashAttribute("modalCrearError", true);
        return "redirect:/seguridad/usuarios";
    }

    private String handleEditError(Integer id, UsuarioDTO.Request request, BindingResult result,
                                   RedirectAttributes redirectAttributes, String errorMsg) {
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuario", result);
        redirectAttributes.addFlashAttribute("usuario", request);
        redirectAttributes.addFlashAttribute("errorUsuarioId", id);
        redirectAttributes.addFlashAttribute("error", errorMsg);
        return "redirect:/seguridad/usuarios";
    }

    private void addDeleteErrorFlash(Integer id, RedirectAttributes redirectAttributes, String errorMsg) {
        redirectAttributes.addFlashAttribute("error", errorMsg);
        try {
            UsuarioDTO.Response usuarioResp = usuarioService.getUsuarioById(id);
            redirectAttributes.addFlashAttribute("errorUsuarioDeleteNombre", usuarioResp.getUsuario());
        } catch (Exception ignored) {}
        redirectAttributes.addFlashAttribute("errorUsuarioDeleteId", id);
    }
}
