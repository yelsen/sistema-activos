package pe.edu.unasam.activos.modules.sistema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.TipoDato;

public class ConfiguracionDTO {

    @Data
    @Builder
    public static class Response {
        private Integer idConfiguracionSistema;
        private String claveConfig;
        private String valorConfig;
        private String descripcionConfig;
        private TipoDato tipoDato;
        private String categoriaConfig;
        private String usuarioModificacion;
    }

    @Data
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "La clave de configuración es obligatoria")
        @Size(max = 100, message = "La clave no puede exceder los 100 caracteres")
        private String claveConfig;

        private String valorConfig;

        private String descripcionConfig;

        @NotNull(message = "El tipo de dato es obligatorio")
        private TipoDato tipoDato;

        @Size(max = 45, message = "La categoría no puede exceder los 45 caracteres")
        private String categoriaConfig;

        // El usuario se gestionará internamente, no se expone en el request.
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FilterRequest {
        private String claveConfig;
        private String categoriaConfig;
    }

    // DTO para la actualización masiva desde el formulario
    public record BulkUpdateRequest(java.util.Map<String, String> configs) {
    }
}