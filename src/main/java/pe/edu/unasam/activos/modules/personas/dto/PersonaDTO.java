package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import pe.edu.unasam.activos.common.enums.Genero;
import pe.edu.unasam.activos.common.enums.EstadoPersona;

public class PersonaDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El número de documento no puede estar en blanco")
        @Size(max = 20, message = "El número de documento no puede exceder 20 caracteres")
        private String dni;

        @NotBlank(message = "El apellido no puede estar en blanco")
        @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
        private String apellidos;

        @NotBlank(message = "El nombre no puede estar en blanco")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        private String nombres;

        @Email(message = "El email no es válido")
        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        private String email;

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        private String telefono;

        @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
        private String direccion;

        @NotNull(message = "El género no puede ser nulo")
        private Genero genero;

        @NotNull(message = "El estado no puede ser nulo")
        private EstadoPersona estadoPersona;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String dni;
        private String apellidos;
        private String nombres;
        private String email;
        private String telefono;
        private String direccion;
        private Genero genero;
        private EstadoPersona estadoPersona;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonaUsuarioResponse {
        private String nombres;
        private String apellidos;
        private String email;
        private String telefono;
        private String direccion;
        private String genero;
        private boolean tieneUsuario;
    }
}
