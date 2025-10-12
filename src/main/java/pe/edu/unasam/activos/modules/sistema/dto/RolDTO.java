package pe.edu.unasam.activos.modules.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoRol;

import java.util.List;

public class RolDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String nombreRol;
        private String descripcionRol;
        private Integer nivelAcceso;
        private String colorRol;
        private EstadoRol estadoRol;
        private List<String> permisosSeleccionados;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idRol;
        private String nombreRol;
        private String descripcionRol;
        private Integer nivelAcceso;
        private String colorRol;
        private EstadoRol estadoRol;
        private Long usuariosCount;
        private Long permisosCount;
        private List<RolPermisoResponse> permisos;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RolPermisoResponse {
            private PermisoDTO.Response permiso;
            private boolean permitido;
        }
    }
}