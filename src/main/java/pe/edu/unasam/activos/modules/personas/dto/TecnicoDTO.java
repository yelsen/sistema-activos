package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoTecnico;

public class TecnicoDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El DNI de la persona es obligatorio")
        private String dniPersona;

        @NotNull(message = "La especialidad es obligatoria")
        private Integer idEspecialidad;

        @NotNull(message = "El estado es obligatorio")
        private EstadoTecnico estadoTecnico;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idTecnico;
        private String dniPersona;
        private String nombreEspecialidad;
        private EstadoTecnico estadoTecnico;
    }
}
