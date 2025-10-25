package pe.edu.unasam.activos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
@EnableSpringDataWebSupport
public class PaginationConfig {

    private final PaginationProperties paginationProperties;

    public PaginationConfig(PaginationProperties paginationProperties) {
        this.paginationProperties = paginationProperties;
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {
        return resolver -> {
            resolver.setFallbackPageable(PageRequest.of(0, paginationProperties.getDefaultSize()));
            resolver.setMaxPageSize(paginationProperties.getMaxSize());
            resolver.setOneIndexedParameters(false);
            resolver.setPageParameterName("page");
            resolver.setSizeParameterName("size");
        };
    }
}
