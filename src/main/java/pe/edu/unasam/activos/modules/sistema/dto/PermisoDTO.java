package pe.edu.unasam.activos.modules.sistema.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
        
        @NotBlank(message = "El nombre del permiso es obligatorio.")
        @Size(max = 150, message = "El nombre del permiso no puede exceder los 150 caracteres.")
        private String nombrePermiso;

        @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres.")
        private String descripcionPermiso;

        @NotNull(message = "El ID del módulo es obligatorio.")
        private Integer moduloId;

        @NotNull(message = "El ID de la acción es obligatorio.")
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
