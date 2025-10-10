package pe.edu.unasam.activos.modules.apis.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unasam.activos.common.dto.ApiResponse;
import pe.edu.unasam.activos.modules.apis.dto.ApiRequest;
import pe.edu.unasam.activos.modules.apis.dto.ApiDto;
import pe.edu.unasam.activos.modules.apis.dto.ApiIntegracionDto;
import pe.edu.unasam.activos.modules.apis.service.ApiService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/apis")
@RequiredArgsConstructor
@Slf4j
public class ApiManagementController {
    
    private final ApiService apiService;
    
    @GetMapping
    @PreAuthorize("hasAuthority('APIS_LEER')")
    public ResponseEntity<ApiResponse<List<ApiDto>>> getAll() {
        log.info("API REST: Obteniendo todas las APIs");
        List<ApiDto> apis = apiService.findAll();
        
        ApiResponse<List<ApiDto>> response = ApiResponse.<List<ApiDto>>builder()
                .success(true)
                .message("APIs obtenidas exitosamente")
                .data(apis)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/paginated")
    @PreAuthorize("hasAuthority('APIS_LEER')")
    public ResponseEntity<ApiResponse<Page<ApiDto>>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idApi") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        log.info("API REST: Obteniendo APIs paginadas");
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? 
                    Sort.by(sortBy).ascending() : 
                    Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ApiDto> apisPage = apiService.findAllPaginated(pageable);
        
        ApiResponse<Page<ApiDto>> response = ApiResponse.<Page<ApiDto>>builder()
                .success(true)
                .message("APIs paginadas obtenidas exitosamente")
                .data(apisPage)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('APIS_VER')")
    public ResponseEntity<ApiResponse<ApiDto>> getById(@PathVariable Integer id) {
        log.info("API REST: Obteniendo API por ID: {}", id);
        ApiDto api = apiService.findById(id);
        
        ApiResponse<ApiDto> response = ApiResponse.<ApiDto>builder()
                .success(true)
                .message("API encontrada")
                .data(api)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('APIS_LEER')")
    public ResponseEntity<ApiResponse<List<ApiDto>>> search(@RequestParam String nombre) {
        
        log.info("API REST: Buscando APIs por nombre: {}", nombre);
        List<ApiDto> apis = apiService.searchByNombre(nombre);
        
        ApiResponse<List<ApiDto>> response = ApiResponse.<List<ApiDto>>builder()
                .success(true)
                .message("Búsqueda completada")
                .data(apis)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('APIS_CREAR')")
    public ResponseEntity<ApiResponse<ApiDto>> create(@Valid @RequestBody ApiRequest request) {
        
        log.info("API REST: Creando nueva API");
        ApiDto api = apiService.create(request);
        
        ApiResponse<ApiDto> response = ApiResponse.<ApiDto>builder()
                .success(true)
                .message("API creada exitosamente")
                .data(api)
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('APIS_EDITAR')")
    public ResponseEntity<ApiResponse<ApiDto>> update(
            @PathVariable Integer id,
            @Valid @RequestBody ApiRequest request) {
        
        log.info("API REST: Actualizando API ID: {}", id);
        ApiDto api = apiService.update(id, request);
        
        ApiResponse<ApiDto> response = ApiResponse.<ApiDto>builder()
                .success(true)
                .message("API actualizada exitosamente")
                .data(api)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('APIS_ELIMINAR')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        log.info("API REST: Eliminando API ID: {}", id);
        apiService.delete(id);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("API eliminada exitosamente")
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    // ==================== ENDPOINTS DE INTEGRACIONES ====================
    
    @GetMapping("/{id}/integraciones")
    @PreAuthorize("hasAuthority('APIS_LEER')")
    public ResponseEntity<ApiResponse<List<ApiIntegracionDto>>> getIntegracionesByApi(@PathVariable Integer id) {
        log.info("API REST: Obteniendo integraciones de la API ID: {}", id);
        List<ApiIntegracionDto> integraciones = apiService.findIntegracionesByApi(id);
        
        ApiResponse<List<ApiIntegracionDto>> response = ApiResponse.<List<ApiIntegracionDto>>builder()
                .success(true)
                .message("Integraciones obtenidas exitosamente")
                .data(integraciones)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/aplicativo/{idAplicativo}/integraciones")
    @PreAuthorize("hasAuthority('APIS_LEER')")
    public ResponseEntity<ApiResponse<List<ApiIntegracionDto>>> getIntegracionesByAplicativo(
            @PathVariable Integer idAplicativo) {
        
        log.info("API REST: Obteniendo integraciones del aplicativo ID: {}", idAplicativo);
        List<ApiIntegracionDto> integraciones = apiService.findIntegracionesByAplicativo(idAplicativo);
        
        ApiResponse<List<ApiIntegracionDto>> response = ApiResponse.<List<ApiIntegracionDto>>builder()
                .success(true)
                .message("Integraciones obtenidas exitosamente")
                .data(integraciones)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/integraciones")
    @PreAuthorize("hasAuthority('APIS_CREAR')")
    public ResponseEntity<ApiResponse<ApiIntegracionDto>> createIntegracion(
            @Valid @RequestBody ApiIntegracionDto dto) {
        
        log.info("API REST: Creando integración API-Aplicativo");
        ApiIntegracionDto integracion = apiService.createIntegracion(dto);
        
        ApiResponse<ApiIntegracionDto> response = ApiResponse.<ApiIntegracionDto>builder()
                .success(true)
                .message("Integración creada exitosamente")
                .data(integracion)
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @DeleteMapping("/integraciones/{idApi}/{idAplicativo}")
    @PreAuthorize("hasAuthority('APIS_ELIMINAR')")
    public ResponseEntity<ApiResponse<Void>> deleteIntegracion(
            @PathVariable Integer idApi,
            @PathVariable Integer idAplicativo) {
        
        log.info("API REST: Eliminando integración API {} - Aplicativo {}", idApi, idAplicativo);
        apiService.deleteIntegracion(idApi, idAplicativo);
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Integración eliminada exitosamente")
                .build();
        
        return ResponseEntity.ok(response);
    }
}