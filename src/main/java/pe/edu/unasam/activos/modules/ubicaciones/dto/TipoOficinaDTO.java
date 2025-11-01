package pe.edu.unasam.activos.modules.ubicaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TipoOficinaDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "El tipo de oficina es obligatorio")
        @Size(max = 100, message = "El tipo de oficina no debe exceder 100 caracteres")
        private String tipoOficina;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idTipoOficina;
        private String tipoOficina;
    }
}
