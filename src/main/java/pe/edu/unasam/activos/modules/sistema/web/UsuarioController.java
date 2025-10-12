package pe.edu.unasam.activos.modules.sistema.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.modules.sistema.dto.UsuarioDTO;
import pe.edu.unasam.activos.modules.sistema.service.RolService;
import pe.edu.unasam.activos.modules.sistema.service.UsuarioService;

@Controller
@RequestMapping("/seguridad/usuarios")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService; // Para poblar el dropdown de roles

    @GetMapping
    @PreAuthorize("hasAuthority('USUARIOS_LEER')")
    public String listUsuarios(Model model, @PageableDefault(size = 10) Pageable pageable) {
        model.addAttribute("page", usuarioService.getAllUsuarios(pageable));
        model.addAttribute("roles", rolService.findAll(null, null, Pageable.unpaged()).getContent());
        return "sistema/usuarios/index";
    }

    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('USUARIOS_LEER')")
    public ResponseEntity<UsuarioDTO.Response> getUsuarioById(@PathVariable Integer id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USUARIOS_CREAR')")
    public String createUsuario(UsuarioDTO.Request request) {
        usuarioService.createUsuario(request);
        return "redirect:/seguridad/usuarios";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_EDITAR')")
    public String updateUsuario(@PathVariable Integer id, UsuarioDTO.Request request) {
        usuarioService.updateUsuario(id, request);
        return "redirect:/seguridad/usuarios";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USUARIOS_ELIMINAR')")
    public String deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return "redirect:/seguridad/usuarios";
    }
}