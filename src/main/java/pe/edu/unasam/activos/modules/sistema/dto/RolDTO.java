package pe.edu.unasam.activos.modules.sistema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoRol;
import pe.edu.unasam.activos.modules.sistema.dto.RolPermisoDTO.PermisoAgrupadoResponse;

import java.util.List;

public class RolDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre del rol debe tener entre 3 y 100 caracteres")
        private String nombreRol;

        @NotBlank(message = "La descripción del rol es obligatoria")
        @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
        private String descripcionRol;

        @NotNull(message = "El nivel de acceso es obligatorio")
        private Integer nivelAcceso;

        @NotBlank(message = "El color del rol es obligatorio")
        @Size(min = 7, max = 7, message = "El color debe estar en formato hexadecimal (#RRGGBB)")
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
        private List<RolPermisoDTO.Response> permisos;
        private List<PermisoAgrupadoResponse> permisosAgrupados;
    }
}
