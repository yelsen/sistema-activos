package pe.edu.unasam.activos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        
        // Configuración para librerías con @ en el nombre
        registry.addResourceHandler("/libs/@tabler/**")
                .addResourceLocations("classpath:/static/libs/@tabler/")
                .setCachePeriod(3600)
                .resourceChain(true);
        
        registry.addResourceHandler("/libs/@popperjs/**")
                .addResourceLocations("classpath:/static/libs/@popperjs/")
                .setCachePeriod(3600)
                .resourceChain(true);
        
        registry.addResourceHandler("/libs/@yaireo/**")
                .addResourceLocations("classpath:/static/libs/@yaireo/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {

    }
}
