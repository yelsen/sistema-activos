package pe.edu.unasam.activos.modules.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.EstadoModulo;

public class ModuloSistemaDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = "idModuloSistemas")
    public static class Response {
        private Integer idModuloSistemas;
        private String nombreModulo;
        private String descripcionModulo;
        private String iconoModulo;
        private EstadoModulo estadoModulo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdenRequest {
        private Integer id;
        private int orden;
    }

}
