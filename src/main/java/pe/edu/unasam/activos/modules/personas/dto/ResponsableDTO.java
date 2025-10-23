package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoResponsable;

import java.time.LocalDate;

public class ResponsableDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull(message = "El documento de la persona es obligatorio")
        private String documentoPersona;

        @NotNull(message = "El cargo es obligatorio")
        private Integer idCargo;

        @NotNull(message = "La oficina es obligatoria")
        private Integer idOficina;

        private LocalDate fechaAsignacion;
        private LocalDate fechaFinAsignacion;
        private Boolean esResponsablePrincipal;

        @NotNull(message = "El estado es obligatorio")
        private EstadoResponsable estadoResponsable;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idResponsable;
        private String nombrePersona;
        private String documentoPersona;
        private String nombreCargo;
        private String nombreOficina;
        private LocalDate fechaAsignacion;
        private LocalDate fechaFinAsignacion;
        private Boolean esResponsablePrincipal;
        private EstadoResponsable estadoResponsable;
    }
}
