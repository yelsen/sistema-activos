package pe.edu.unasam.activos.modules.proveedores.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoProveedor;

public class ProveedorDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El RUC del proveedor es obligatorio")
        @Size(max = 13, message = "El RUC no puede exceder 13 caracteres")
        private String rucProveedor;

        @NotBlank(message = "El nombre del proveedor es obligatorio")
        @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
        private String nombreProveedor;

        @Size(max = 1000, message = "La razón social no debe exceder 1000 caracteres")
        private String razonSocial;

        @Size(max = 1000, message = "La dirección no debe exceder 1000 caracteres")
        private String direccion;

        @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
        private String telefono;

        @Email(message = "El email no es válido")
        @Size(max = 100, message = "El email no debe exceder 100 caracteres")
        private String email;

        private EstadoProveedor estadoProveedor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idProveedor;
        private String rucProveedor;
        private String nombreProveedor;
        private String razonSocial;
        private String direccion;
        private String telefono;
        private String email;
        private EstadoProveedor estadoProveedor;
    }
}
