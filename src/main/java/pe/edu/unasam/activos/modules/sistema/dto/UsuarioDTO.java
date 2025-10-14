package pe.edu.unasam.activos.modules.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoUsuario;
import java.time.LocalDateTime;

public class UsuarioDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String usuario;
        private String contrasena;
        private Boolean debeCambiarPassword;
        private EstadoUsuario estadoUsuarios;
        private String documentoPersona;
        private Integer idRol;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idUsuario;
        private String usuario;
        private String ultimoAcceso;
        private Integer intentosFallidos;
        private LocalDateTime bloqueadoHasta;
        private Boolean debeCambiarPassword;
        private EstadoUsuario estadoUsuarios;
        private String nombrePersona;
        private String documentoPersona;
        private String nombreRol;
        private Integer idRol;
    }
}
