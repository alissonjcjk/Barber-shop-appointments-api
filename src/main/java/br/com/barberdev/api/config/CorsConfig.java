package br.com.barberdev.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * CORS configuration.
 *
 * Allowed origins are injected via the {@code cors.allowed-origins} property,
 * making it easy to restrict origins per environment:
 *
 * <pre>
 * # application-dev.yml  → libera qualquer origem durante desenvolvimento
 * cors.allowed-origins: "*"
 *
 * # application-prod.yml → restringe à origem real do front-end
 * cors.allowed-origins: "https://barberdev.com.br"
 * </pre>
 */
@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean(
            final CorsProperties corsProperties) {

        final var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(corsProperties.getAllowedOrigins());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Location", "Content-Disposition"));
        // Cache preflight response for 30 min — reduces unnecessary OPTIONS round-trips
        config.setMaxAge(1800L);

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        final var bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
