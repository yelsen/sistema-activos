package pe.edu.unasam.activos.modules.personas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoTecnico;
import pe.edu.unasam.activos.common.enums.Genero;
import jakarta.validation.constraints.Size;

public class TecnicoDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El DNI de la persona es obligatorio")
        @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
        private String dniPersona;

        @NotNull(message = "La especialidad es obligatoria")
        private Integer idEspecialidad;

        // Campos para crear/actualizar persona
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

        private EstadoTecnico estadoTecnico;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idTecnico;
        private String dniPersona;
        private String nombrePersona;
        private String emailPersona;
        private String telefonoPersona;
        private String direccionPersona;
        private Genero generoPersona;

        private Integer idEspecialidad;
        private String nombreEspecialidad;
        private EstadoTecnico estadoTecnico;

        // Campos para formulario de edición
        private String nombres;
        private String apellidos;
        private String email;
        private String telefono;
        private String direccion;
        private Genero genero;
    }
}
