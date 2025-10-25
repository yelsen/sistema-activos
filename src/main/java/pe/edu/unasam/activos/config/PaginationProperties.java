package pe.edu.unasam.activos.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PaginationProperties {

    private Integer defaultSize = 5;
    private Integer maxSize = 100;
    private List<Integer> allowedSizes = new ArrayList<>(Arrays.asList(5, 10, 20, 50));

    public Integer getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(Integer defaultSize) {
        this.defaultSize = defaultSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public List<Integer> getAllowedSizes() {
        return allowedSizes;
    }

    public void setAllowedSizes(List<Integer> allowedSizes) {
        this.allowedSizes = allowedSizes;
    }

    /**
     * Normaliza el size solicitado a uno permitido. Si no coincide, devuelve
     * default.
     */
    public int normalizeSize(Integer requested) {
        if (requested == null || requested <= 0) {
            return defaultSize;
        }

        int bounded = Math.min(requested, maxSize);
        return allowedSizes.contains(bounded) ? bounded : defaultSize;
    }
}
