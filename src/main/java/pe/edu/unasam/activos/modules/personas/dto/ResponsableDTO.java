package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoResponsable;
import pe.edu.unasam.activos.common.enums.Genero;

import java.time.LocalDate;

public class ResponsableDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El DNI de la persona es obligatorio")
        @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
        private String dniPersona;

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100)
        private String nombres;

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100)
        private String apellidos;

        @NotBlank(message = "El email es obligatorio")
        @Size(max = 100)
        private String email;

        @Size(max = 20)
        private String telefono;

        @Size(max = 200)
        private String direccion;

        private Genero genero;
        
        @NotNull(message = "El cargo es obligatorio")
        private Integer idCargo;

        private Integer idOficina;
        private LocalDate fechaAsignacion;
        private LocalDate fechaFinAsignacion;
        private EstadoResponsable estadoResponsable;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idResponsable;
        private String nombrePersona;
        private String dniPersona;
        private String nombreCargo;
        private String nombreOficina;
        private LocalDate fechaAsignacion;
        private LocalDate fechaFinAsignacion;
        private EstadoResponsable estadoResponsable;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditForm {
        private Integer idResponsable;
        private String dniPersona;
        private String nombres;
        private String apellidos;
        private String email;
        private String telefono;
        private String direccion;
        private String genero;
        private Integer idCargo;
        private Integer idOficina;
    }
}
