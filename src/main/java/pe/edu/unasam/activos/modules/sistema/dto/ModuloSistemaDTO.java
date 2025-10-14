package pe.edu.unasam.activos.modules.sistema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import pe.edu.unasam.activos.common.enums.EstadoModulo;
import java.time.LocalDateTime;

public class ModuloSistemaDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        
        private Integer idModuloSistemas;
        private String nombreModulo;
        private String descripcionModulo;
        private String iconoModulo;
        private String rutaModulo;
        private Integer ordenModulo;
        private EstadoModulo estadoModulo;
        private String estadoModuloTexto;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private Integer idModuloSistemas;

        @NotBlank(message = "El nombre del módulo es obligatorio")
        @Size(max = 45, message = "El nombre no puede exceder 45 caracteres")
        private String nombreModulo;

        private String descripcionModulo;

        @Size(max = 100, message = "El icono no puede exceder 100 caracteres")
        private String iconoModulo;

        @Size(max = 100, message = "La ruta no puede exceder 100 caracteres")
        private String rutaModulo;

        private Integer ordenModulo;

        @NotNull(message = "El estado del módulo es obligatorio")
        private EstadoModulo estadoModulo;

        private Integer idModuloPadre;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FilterRequest {
        private String nombreModulo;
        private EstadoModulo estadoModulo;
        private String rutaModulo;
    }
}
