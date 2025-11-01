package pe.edu.unasam.activos.modules.ubicaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrigenDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El nombre del origen es obligatorio")
        @Size(max = 45, message = "El nombre no puede exceder 45 caracteres")
        private String nombreOrigen;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idOrigen;
        private String nombreOrigen;
    }
}
