package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class EspecialidadTecnicoDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El nombre no puede estar en blanco")
        @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
        private String nombreEspecialidad;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idEspecialidadTecnico;
        private String nombreEspecialidad;
    }
}
