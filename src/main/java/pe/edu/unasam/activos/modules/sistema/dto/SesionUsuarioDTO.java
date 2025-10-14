package pe.edu.unasam.activos.modules.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoSesion;

public class SesionUsuarioDTO {

    @Data
    public static class Request {
        // Generalmente no se necesita, las sesiones se crean internamente
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idSesionUsuario;
        private String nombreCompleto;
        private String userAgent;
        private String fechaInicio;
        private String fechaUltimoAcceso;
        private EstadoSesion estadoSesion;
    }
}
