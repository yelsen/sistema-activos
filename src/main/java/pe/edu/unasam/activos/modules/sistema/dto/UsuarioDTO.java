package pe.edu.unasam.activos.modules.sistema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import java.time.LocalDateTime;

public class UsuarioDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 100, message = "El nombre de usuario no debe exceder los 100 caracteres")
        private String usuario;
        
        @NotBlank(message = "La contraseña es obligatoria")
        private String contrasena;

        private Boolean debeCambiarPassword;
        
        @NotNull(message = "El estado del usuario es obligatorio")
        private EstadoUsuario estadoUsuarios;
        
        @NotBlank(message = "El documento de la persona es obligatorio")
        @Size(min = 8, max = 20, message = "El documento de la persona debe tener entre 8 y 20 caracteres")
        private String documentoPersona;
        
        @NotNull(message = "El ID del rol es obligatorio")
        private Integer idRol;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idUsuario;
        private String usuario;
        private String ultimoAcceso;
        private Integer intentosFallidos;
        private LocalDateTime bloqueadoHasta;
        private Boolean debeCambiarPassword;
        private EstadoUsuario estadoUsuarios;
        private String nombrePersona;
        private String documentoPersona;
        private String nombreRol;
        private Integer idRol;
    }
}
