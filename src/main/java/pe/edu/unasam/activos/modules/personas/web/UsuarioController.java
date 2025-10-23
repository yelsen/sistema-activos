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

import jakarta.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import pe.edu.unasam.activos.modules.personas.dto.UsuarioDTO;
import pe.edu.unasam.activos.modules.personas.service.UsuarioService;
import pe.edu.unasam.activos.modules.sistema.service.RolService;
import pe.edu.unasam.activos.modules.personas.service.TipoDocumentoService;
import pe.edu.unasam.activos.common.exception.BusinessException;

import java.beans.PropertyEditorSupport;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/seguridad/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final TipoDocumentoService tipoDocumentoService;
    private final ObjectMapper objectMapper;

    /**
     * Registra un editor de propiedades personalizado para el enum EstadoUsuario.
     * Esto permite que Spring MVC convierta una cadena vacía a null, evitando errores
     * cuando el parámetro 'estado' no se selecciona en el frontend.
     */
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
            Pageable pageable) {
        
        try {
            // Obtener usuarios con filtros
            Page<UsuarioDTO.Response> usuariosPage = usuarioService.getAllUsuarios(query, estado, pageable);
            model.addAttribute("usuariosPage", usuariosPage);
            model.addAttribute("query", query);
            model.addAttribute("estado", estado);

            // Construir queryParams para paginación
            UriComponentsBuilder queryParamsBuilder = UriComponentsBuilder.newInstance();
            if (query != null && !query.isBlank()) {
                queryParamsBuilder.queryParam("query", query);
            }
            if (estado != null) {
                queryParamsBuilder.queryParam("estado", estado.name());
            }
            // Pasamos la cadena de consulta, no el mapa. Es más seguro para Thymeleaf.
            model.addAttribute("queryParams", queryParamsBuilder.build().getQuery());

            // El mapa de colores es necesario tanto para la carga completa como para los fragmentos
            var roles = rolService.findAllRolesForSelect();
            Map<Integer, String> rolColorMap = roles.stream()
                    .collect(Collectors.toMap(
                            rol -> rol.getIdRol(),
                            rol -> rol.getColorRol()
                    ));
            model.addAttribute("rolColorMap", rolColorMap);

            // Solo cargar datos adicionales si NO es una petición de fragmento
            if (!fragment) {
                model.addAttribute("roles", roles);
                model.addAttribute("tiposDocumento", tipoDocumentoService.findAllTiposDocumento());
                model.addAttribute("estadosUsuario", EstadoUsuario.values());
                model.addAttribute("rolColorMapJson", objectMapper.writeValueAsString(rolColorMap));

                if (!model.containsAttribute("usuario")) { // Objeto para formulario de creación
                    model.addAttribute("usuario", new UsuarioDTO.Request());
                }
                return "sistema/usuarios/index";
            } else {
                // Retornar solo el fragmento de la lista para peticiones AJAX
                return "sistema/usuarios/usuarios-list :: usuariosList";
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al cargar usuarios: " + e.getMessage(), e);
        }
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_EDITAR')")
    public String getEditUsuarioForm(@PathVariable Integer id, Model model) {
        UsuarioDTO.Response usuario = usuarioService.getUsuarioById(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.findAllRolesForSelect());
        model.addAttribute("tiposDocumento", tipoDocumentoService.findAllTiposDocumento());
        return "sistema/usuarios/modal/EditarForm :: editForm";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USUARIOS_CREAR')")
    public String createUsuario(
            @Valid @ModelAttribute("usuario") UsuarioDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuario", result);
            redirectAttributes.addFlashAttribute("usuario", request);
            redirectAttributes.addFlashAttribute("modalCrearError", true);
            return "redirect:/seguridad/usuarios";
        }
        
        try {
            usuarioService.createUsuario(request);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuario", result);
            redirectAttributes.addFlashAttribute("usuario", request);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("modalCrearError", true);
        }
        
        return "redirect:/seguridad/usuarios";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_EDITAR')")
    public String updateUsuario(
            @PathVariable Integer id,
            @Valid @ModelAttribute("usuario") UsuarioDTO.Request request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuario", result);
            redirectAttributes.addFlashAttribute("usuario", request);
            redirectAttributes.addFlashAttribute("errorUsuarioId", id);
            return "redirect:/seguridad/usuarios";
        }
        
        try {
            usuarioService.updateUsuario(id, request);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorUsuarioId", id);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
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
            usuarioService.deleteUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/seguridad/usuarios";
    }
}
