package pe.edu.unasam.activos.initialization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializerService {

    private final List<AbstractDataLoader> dataLoaders;

    @PostConstruct
    public void init() {
        this.initialize();
    }
    
    public void initialize() {
        log.info("╔══════════════════════════════════════════════════════════════╗");
        log.info("║           INICIANDO CARGA DE DATOS INICIALES                ║");
        log.info("╚══════════════════════════════════════════════════════════════╝");
        
        // Ordenar los loaders según la anotación @Order
        dataLoaders.sort(AnnotationAwareOrderComparator.INSTANCE);
        
        // Ejecutar cada loader
        for (AbstractDataLoader loader : dataLoaders) {
            try {
                loader.execute();
            } catch (Exception e) {
                log.error("Error ejecutando loader: {}", loader.getLoaderName(), e);
            }
        }
           
        log.info("╔══════════════════════════════════════════════════════════════╗");
        log.info("║          CARGA DE DATOS INICIALES COMPLETADA                ║");
        log.info("╚══════════════════════════════════════════════════════════════╝");
    }
}
