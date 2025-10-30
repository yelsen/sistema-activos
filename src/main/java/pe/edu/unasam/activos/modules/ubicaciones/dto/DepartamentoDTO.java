package pe.edu.unasam.activos.modules.ubicaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DepartamentoDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "El nombre del departamento es obligatorio")
        @Size(max = 255, message = "El nombre del departamento no debe exceder 255 caracteres")
        private String nombreDepartamento;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idDepartamento;
        private String nombreDepartamento;
    }
}
