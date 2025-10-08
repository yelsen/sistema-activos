package pe.edu.unasam.activos.modules.apis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unasam.activos.common.exception.NotFoundException;
import pe.edu.unasam.activos.common.exception.BadRequestException;
import pe.edu.unasam.activos.modules.apis.domain.Api;
import pe.edu.unasam.activos.modules.apis.domain.ApiAplicativo;
import pe.edu.unasam.activos.modules.apis.domain.ApiAplicativoId;
import pe.edu.unasam.activos.modules.apis.repository.ApiRepository;
import pe.edu.unasam.activos.modules.apis.repository.ApiAplicativoRepository;
import pe.edu.unasam.activos.modules.apis.dto.ApiRequest;
import pe.edu.unasam.activos.modules.apis.dto.ApiDto;
import pe.edu.unasam.activos.modules.apis.dto.ApiIntegracionDto;
import pe.edu.unasam.activos.modules.aplicativos.repository.AplicativoRepository;
import pe.edu.unasam.activos.modules.aplicativos.domain.Aplicativo;

import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApiService {
    
    private final ApiRepository apiRepository;
    private final ApiAplicativoRepository apiAplicativoRepository;
    private final AplicativoRepository aplicativoRepository;
    
    public List<ApiDto> findAll() {
        log.info("Buscando todas las APIs");
        List<Api> apis = apiRepository.findAll();
        if (apis.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Optimización N+1: Obtener todos los conteos en una sola consulta
        List<Integer> apiIds = apis.stream().map(Api::getIdApi).collect(Collectors.toList());
        Map<Integer, Long> conteos = apiAplicativoRepository.countIntegracionesByApiIds(apiIds).stream()
                .collect(Collectors.toMap(result -> (Integer) result[0], result -> (Long) result[1]));

        return apis.stream()
                .map(api -> convertToDto(api, conteos.getOrDefault(api.getIdApi(), 0L)))
                .collect(Collectors.toList());
    }
    
    public Page<ApiDto> findAllPaginated(Pageable pageable) {
        log.info("Buscando APIs paginadas");
        return apiRepository.findAll(pageable)
                .map(this::convertToDto);
    }
    
    public ApiDto findById(Integer id) {
        log.info("Buscando API por ID: {}", id);
        Api api = apiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("API no encontrada con ID: " + id));
        return convertToDto(api);
    }
    
    public List<ApiDto> searchByNombre(String nombre) {
        log.info("Buscando APIs por nombre: {}", nombre);
        return apiRepository.findByNombreApiContainingIgnoreCase(nombre).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ApiDto create(ApiRequest request) {
        log.info("Creando nueva API: {}", request.getNombreApi());
        
        // Validar que el nombre no exista
        if (apiRepository.existsByNombreApi(request.getNombreApi())) {
            throw new BadRequestException("Ya existe una API con el nombre: " + request.getNombreApi());
        }
        
        Api api = Api.builder()
                .nombreApi(request.getNombreApi())
                .descripcionApi(request.getDescripcionApi())
                .urlApi(request.getUrlApi())
                .versionApi(request.getVersionApi())
                .metodoAutenticacion(request.getMetodoAutenticacion())
                .build();
        
        Api apiGuardada = apiRepository.save(api);
        log.info("API creada exitosamente con ID: {}", apiGuardada.getIdApi());
        
        return convertToDto(apiGuardada);
    }
    
    @Transactional
    public ApiDto update(Integer id, ApiRequest request) {
        log.info("Actualizando API ID: {}", id);
        
        Api api = apiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("API no encontrada con ID: " + id));
        
        // Validar nombre único si cambió
        if (!api.getNombreApi().equals(request.getNombreApi())) {
            if (apiRepository.existsByNombreApi(request.getNombreApi())) {
                throw new BadRequestException("Ya existe una API con el nombre: " + request.getNombreApi());
            }
        }
        
        api.setNombreApi(request.getNombreApi());
        api.setDescripcionApi(request.getDescripcionApi());
        api.setUrlApi(request.getUrlApi());
        api.setVersionApi(request.getVersionApi());
        api.setMetodoAutenticacion(request.getMetodoAutenticacion());
        
        Api apiActualizada = apiRepository.save(api);
        log.info("API actualizada exitosamente ID: {}", id);
        
        return convertToDto(apiActualizada);
    }
    
    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando API ID: {}", id);
        
        if (!apiRepository.existsById(id)) {
            throw new NotFoundException("API no encontrada con ID: " + id);
        }
        
        // Verificar si tiene integraciones activas
        Long cantidadIntegraciones = apiAplicativoRepository.countIntegracionesByApi(id);
        if (cantidadIntegraciones > 0) {
            throw new BadRequestException("No se puede eliminar la API porque tiene " + 
                    cantidadIntegraciones + " integraciones asociadas");
        }
        
        apiRepository.deleteById(id);
        log.info("API eliminada exitosamente ID: {}", id);
    }
    
    // ==================== GESTIÓN DE INTEGRACIONES ====================
    
    public List<ApiIntegracionDto> findIntegracionesByApi(Integer idApi) {
        log.info("Buscando integraciones de la API ID: {}", idApi);
        return apiAplicativoRepository.findByApi_IdApi(idApi).stream()
                .map(this::convertToIntegracionDto)
                .collect(Collectors.toList());
    }
    
    public List<ApiIntegracionDto> findIntegracionesByAplicativo(Integer idAplicativo) {
        log.info("Buscando integraciones del aplicativo ID: {}", idAplicativo);
        return apiAplicativoRepository.findByAplicativo_IdAplicativo(idAplicativo).stream()
                .map(this::convertToIntegracionDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ApiIntegracionDto createIntegracion(ApiIntegracionDto dto) {
        log.info("Creando integración API-Aplicativo: API {} - Aplicativo {}", 
                dto.getIdApi(), dto.getIdAplicativo());
        
        // Validar que existan la API y el aplicativo
        Api api = apiRepository.findById(dto.getIdApi())
                .orElseThrow(() -> new NotFoundException("API no encontrada"));
        
        Aplicativo aplicativo = aplicativoRepository.findById(dto.getIdAplicativo())
                .orElseThrow(() -> new NotFoundException("Aplicativo no encontrado"));
        
        ApiAplicativo integracion = ApiAplicativo.builder()
                .api(api)
                .aplicativo(aplicativo)
                .uso(dto.getUso())
                .versionIntegracion(dto.getVersionIntegracion())
                .estadoIntegracion(dto.getEstadoIntegracion())
                .build();
        
        ApiAplicativo integracionGuardada = apiAplicativoRepository.save(integracion);
        log.info("Integración creada exitosamente");
        
        return convertToIntegracionDto(integracionGuardada);
    }
    
    @Transactional
    public void deleteIntegracion(Integer idApi, Integer idAplicativo) {
        log.info("Eliminando integración: API {} - Aplicativo {}", idApi, idAplicativo);
        
        ApiAplicativoId id = new ApiAplicativoId();
        id.setAplicativo(idAplicativo);
        id.setApi(idApi);
        
        ApiAplicativo integracion = apiAplicativoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Integración no encontrada"));
        
        apiAplicativoRepository.delete(integracion);
        log.info("Integración eliminada exitosamente");
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    private ApiDto convertToDto(Api api) {
        // Este método se mantiene para usos individuales como findById
        long cantidadIntegraciones = apiAplicativoRepository.countIntegracionesByApi(api.getIdApi());
        return convertToDto(api, cantidadIntegraciones);
    }

    private ApiDto convertToDto(Api api, long cantidadIntegraciones) {
        return ApiDto.builder()
                .idApi(api.getIdApi())
                .nombreApi(api.getNombreApi())
                .descripcionApi(api.getDescripcionApi())
                .urlApi(api.getUrlApi())
                .versionApi(api.getVersionApi())
                .metodoAutenticacion(api.getMetodoAutenticacion())
                .cantidadIntegraciones(cantidadIntegraciones) // Usar el conteo pre-calculado
                .activa(cantidadIntegraciones > 0)
                .build();
    }
    
    private ApiIntegracionDto convertToIntegracionDto(ApiAplicativo integracion) {
        return ApiIntegracionDto.builder()
                .idApi(integracion.getApi().getIdApi())
                .idAplicativo(integracion.getAplicativo().getIdAplicativo())
                .uso(integracion.getUso())
                .versionIntegracion(integracion.getVersionIntegracion())
                .estadoIntegracion(integracion.getEstadoIntegracion())
                .nombreApi(integracion.getApi().getNombreApi())
                .nombreAplicativo(integracion.getAplicativo().getNombreAplicativo())
                .urlApi(integracion.getApi().getUrlApi())
                .build();
    }
}
