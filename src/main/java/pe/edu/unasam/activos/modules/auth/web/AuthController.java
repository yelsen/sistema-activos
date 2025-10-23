package pe.edu.unasam.activos.modules.auth.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.unasam.activos.modules.auth.dto.LoginRequest;
import pe.edu.unasam.activos.modules.auth.dto.TokenResponse;
import pe.edu.unasam.activos.modules.auth.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthService authService;
    
    @GetMapping({"/", "/login"})
    public String showLoginForm(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        
        if (logout != null) {
            model.addAttribute("success", "Sesión cerrada exitosamente");
        }
        
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            String userAgent = request.getHeader("User-Agent");
            TokenResponse tokenResponse = authService.login(loginRequest, userAgent);
            
            // Guardar el token y la información del usuario en la sesión HTTP
            // El tokenResponse ya contiene el UserInfoResponse con todos los datos cargados.
            // No es necesario volver a consultar la base de datos.
            request.getSession().setAttribute("token", tokenResponse.getToken());
            request.getSession().setAttribute("user", tokenResponse.getUserInfo());
            
            return "redirect:/home";
        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login?error=true";
        }
    }
    
    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/auth/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("token");
        
        if (token != null) {
            try {
                authService.logout(token);
            } catch (Exception e) {
                log.error("Error al cerrar sesión", e);
            }
        }
        
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Sesión cerrada exitosamente");
        
        return "redirect:/login?logout=true";
    }
    
    @GetMapping("/auth/profile")
    public String profile(Authentication authentication, Model model) {
        if (authentication != null) {
            String username = authentication.getName();
            var userInfo = authService.getCurrentUserInfo(username);
            model.addAttribute("userInfo", userInfo);
            return "auth/profile";
        }
        return "redirect:/home";
    }
    
    @GetMapping("/auth/cambiar-password")
    public String showChangePasswordForm(Model model) {
        return "auth/cambiar-password";
    }
    
    @PostMapping("/auth/cambiar-password")
    public String changePassword(
            @RequestParam String passwordActual,
            @RequestParam String passwordNueva,
            @RequestParam String passwordConfirmar,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        try {
            if (!passwordNueva.equals(passwordConfirmar)) {
                redirectAttributes.addFlashAttribute("error", 
                        "Las contraseñas nuevas no coinciden");
                return "redirect:/auth/cambiar-password";
            }
            
            String username = authentication.getName();
            authService.cambiarPassword(username, passwordActual, passwordNueva);
            
            redirectAttributes.addFlashAttribute("success", 
                    "Contraseña cambiada exitosamente");
            return "redirect:/home";
            
        } catch (Exception e) {
            log.error("Error al cambiar contraseña", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/cambiar-password";
        }
    }
}
