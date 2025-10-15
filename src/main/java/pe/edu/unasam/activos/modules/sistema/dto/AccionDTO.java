package pe.edu.unasam.activos.modules.sistema.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccionDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @Size(max = 45, message = "El nombre no puede exceder 45 caracteres")
        private String nombreAccion;

        @Size(max = 45, message = "El código no puede exceder 45 caracteres")
        private String codigoAccion;

        @Size(max = 100, message = "La descripción no puede exceder 100 caracteres")
        private String descripcionAccion;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idAccion;
        private String nombreAccion;
        private String codigoAccion;
        private String descripcionAccion;
    }
}