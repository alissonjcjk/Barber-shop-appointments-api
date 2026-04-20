package br.com.barberdev.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Typed configuration properties for CORS.
 * Bound from the {@code cors.*} namespace in application.yml / profile YAMLs.
 */
@Component
@ConfigurationProperties(prefix = "cors")
@Getter
@Setter
public class CorsProperties {

    /**
     * List of allowed origin patterns.
     * Use {@code "*"} (wildcard) for development.
     * Use specific origins (e.g., {@code "https://barberdev.com.br"}) for production.
     *
     * Default: allows any origin (safe for local dev, override in prod profile).
     */
    private List<String> allowedOrigins = List.of("*");
}
