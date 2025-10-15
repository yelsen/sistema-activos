package pe.edu.unasam.activos.modules.sistema.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class RolPermisoDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull(message = "El ID del rol es obligatorio.")
        private Integer rolId;

        @NotNull(message = "El ID del permiso es obligatorio.")
        private Integer permisoId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private PermisoDTO.Response permiso;
        private boolean permitido;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PermisoAgrupadoResponse {
        private ModuloSistemaDTO.Response modulo;
        private List<AccionDTO.Response> acciones;
    }
}
