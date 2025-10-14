package pe.edu.unasam.activos.modules.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PermisoDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String nombrePermiso;
        private String descripcionPermiso;
        private Integer moduloId;
        private Integer accionId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idPermiso;
        private String codigoPermiso;
        private String nombrePermiso;
        private String descripcionPermiso;
        private ModuloSistemaDTO.Response moduloSistema;
        private AccionDTO.Response accion;
    }
}
