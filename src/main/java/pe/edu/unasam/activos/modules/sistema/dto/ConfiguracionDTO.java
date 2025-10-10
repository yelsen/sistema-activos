package pe.edu.unasam.activos.modules.sistema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.unasam.activos.common.enums.TipoDato;

public class ConfiguracionDTO {

    @Data
    public static class Request {
        private String claveConfig;
        private String valorConfig;
        private String descripcionConfig;
        private TipoDato tipoDato;
        private String categoriaConfig;
        private String usuarioModificacion;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer idConfiguracionSistema;
        private String claveConfig;
        private String valorConfig;
        private String descripcionConfig;
        private TipoDato tipoDato;
        private String categoriaConfig;
        private String usuarioModificacion;
    }
}