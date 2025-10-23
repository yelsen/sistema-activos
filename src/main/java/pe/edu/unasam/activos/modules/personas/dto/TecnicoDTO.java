package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TecnicoDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El documento de la persona es obligatorio")
        private String documentoPersona;

        @NotNull(message = "La especialidad es obligatoria")
        private Integer idEspecialidad;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idTecnico;
        private String nombrePersona;
        private String documentoPersona;
        private String nombreEspecialidad;
    }
}
