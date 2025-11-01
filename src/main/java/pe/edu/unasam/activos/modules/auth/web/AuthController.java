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

    @GetMapping({ "/", "/login" })
    public String showLoginForm(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String locked,
            @RequestParam(required = false) Integer remaining,
            @RequestParam(required = false) Integer max,
            @RequestParam(required = false) Long lockMinutes,
            @RequestParam(required = false, name = "lockedMinutesLeft") Long lockedMinutesLeft,
            Model model) {

        if (error != null) {
            if (remaining != null && max != null) {
                String errMsg = String.format(
                        "Usuario o contraseña incorrectos. Te quedan %d de %d intentos%s.",
                        remaining, max,
                        lockMinutes != null ? " antes de un bloqueo de " + lockMinutes + " minutos" : "");
                model.addAttribute("error", errMsg);
            } else {
                model.addAttribute("error", "Usuario o contraseña incorrectos");
            }
        }

        if (logout != null) {
            model.addAttribute("success", "Sesión cerrada exitosamente");
        }

        if (locked != null) {
            String msg;
            if (lockedMinutesLeft != null) {
                msg = String.format("Cuenta bloqueada. Intenta nuevamente en %d minutos.", lockedMinutesLeft);
            } else if (lockMinutes != null) {
                msg = String.format("Cuenta bloqueada por %d minutos por intentos fallidos.", lockMinutes);
            } else {
                msg = "Cuenta bloqueada temporalmente por intentos fallidos.";
            }
            model.addAttribute("warning", msg);
        }

        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            String userAgent = request.getHeader("User-Agent");
            TokenResponse tokenResponse = authService.login(loginRequest, userAgent);
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

    // ===== Recuperación de contraseña (WhatsApp + Código) =====
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String requestPasswordReset(@RequestParam String username, RedirectAttributes redirectAttributes) {
        try {
            authService.solicitarCodigoRecuperacion(username);
            redirectAttributes.addFlashAttribute("success", "Se envió el código de verificación a tu WhatsApp.");
            return "redirect:/reset-password?username=" + username;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam(required = false) String username, Model model) {
        model.addAttribute("username", username);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPasswordWithCode(
            @RequestParam String username,
            @RequestParam String code,
            @RequestParam String passwordNueva,
            @RequestParam String passwordConfirmar,
            RedirectAttributes redirectAttributes) {

        try {
            if (!passwordNueva.equals(passwordConfirmar)) {
                redirectAttributes.addFlashAttribute("error", "Las contraseñas nuevas no coinciden");
                return "redirect:/reset-password?username=" + username;
            }

            authService.resetPasswordWithCode(username, code, passwordNueva);
            redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente. Inicia sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            log.error("Error al resetear contraseña por código", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reset-password?username=" + username;
        }
    }
}
