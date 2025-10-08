package pe.edu.unasam.activos.initialization;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDataLoader {

    protected abstract String getLoaderName();
    protected abstract void loadData();
    protected abstract boolean shouldLoad();
    
    public void execute() {
        if (shouldLoad()) {
            log.info("========================================");
            log.info("Ejecutando loader: {}", getLoaderName());
            log.info("========================================");
            loadData();
            log.info("Loader {} completado exitosamente", getLoaderName());
        } else {
            log.info("Loader {} omitido - datos ya existen", getLoaderName());
        }
    }
}
