package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TipoDocumentoDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El tipo de documento no puede estar en blanco")
        @Size(max = 45, message = "El tipo de documento no puede exceder 45 caracteres")
        private String tipoDocumento;

        @Size(max = 8, message = "La longitud mínima no puede exceder 8 caracteres")
        private String longitudMinima;

        @Size(max = 12, message = "La longitud máxima no puede exceder 12 caracteres")
        private String longitudMaxima;

        @Size(max = 100, message = "El patrón de validación no puede exceder 100 caracteres")
        private String patronValidacion;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idTipoDocumento;
        private String tipoDocumento;
        private String longitudMinima;
        private String longitudMaxima;
        private String patronValidacion;
    }
}
