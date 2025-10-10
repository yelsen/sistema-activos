package pe.edu.unasam.activos.modules.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccionDTO {

    @Data
    public static class Request {
        private String nombreAccion;
        private String codigoAccion;
        private String descripcionAccion;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idAccion;
        private String nombreAccion;
        private String codigoAccion;
        private String descripcionAccion;
    }
}