package pe.edu.unasam.activos.modules.ubicaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoOficina;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OficinaDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        
        @NotBlank(message = "El nombre de la oficina es obligatorio.")
        @Size(max = 255, message = "El nombre de la oficina no debe exceder 255 caracteres.")
        private String nombreOficina;

        @Size(max = 255, message = "La dirección no debe exceder 255 caracteres.")
        private String direccionOficina;

        @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres.")
        private String telefonoOficina;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idOficina;
        private String nombreOficina;
        private String direccionOficina;
        private String telefonoOficina;
        private EstadoOficina estadoOficina;
    }
}
