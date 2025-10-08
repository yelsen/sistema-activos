package pe.edu.unasam.activos.modules.apis.dto;

import lombok.*;
import pe.edu.unasam.activos.common.enums.MetodoAutenticacion;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class ApiResponse {
    
    private Integer idApi;
    private String nombreApi;
    private String descripcionApi;
    private String urlApi;
    private String versionApi;
    private MetodoAutenticacion metodoAutenticacion;
    
    // Información adicional
    private Long cantidadIntegraciones;
    private Boolean activa;
}
