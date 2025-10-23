package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class CargoDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El nombre no puede estar en blanco")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        private String nombreCargo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idCargo;
        private String nombreCargo;
    }
}
