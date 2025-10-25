package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import pe.edu.unasam.activos.common.enums.Genero;

public class UsuarioDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Solo puede contener letras, números, puntos, guiones y guiones bajos")
        private String usuario;

        @Pattern(regexp = "^$|^.{8,100}$", message = "Dejar en blanco o usar 8 a 100 caracteres")
        private String contrasena;

        @NotNull(message = "El rol es obligatorio")
        private Integer idRol;

        @NotBlank(message = "El DNI es obligatorio")
        @Size(max = 20, message = "El DNI no puede exceder 20 caracteres")
        private String dniPersona;

        // Campos para crear persona (solo si no existe)
        private Integer idTipoDocumento;

        @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
        private String nombres;

        @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
        private String apellidos;

        @Email(message = "El email no es válido")
        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        private String email;

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        private String telefono;

        @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
        private String direccion;

        private Genero genero;

        private EstadoUsuario estadoUsuarios;
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
        private java.time.LocalDateTime bloqueadoHasta;
        private EstadoUsuario estadoUsuarios;

        // Datos de la persona (existentes)
        private String nombrePersona;
        private String dniPersona;
        private String emailPersona;
        private String telefonoPersona;
        private String direccionPersona;
        private Genero generoPersona;

        // Datos del rol
        private String nombreRol;
        private Integer idRol;

        // Campos adicionales para edición (coinciden con UsuarioDTO.Request)
        private String nombres;
        private String apellidos;
        private String email;
        private String telefono;
        private String direccion;
        private Genero genero;
        // Campo opcional para enlace de formulario en edición
        private String contrasena;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleResponse {
        private Integer idUsuario;
        private String usuario;
        private String nombreCompleto;
        private String email;
        private EstadoUsuario estado;
    }
}
