package pe.edu.unasam.activos.modules.ubicaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OficinaDTO {

    public static class Request {
        
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idOficina;
        private String nombreOficina;
    }
}
