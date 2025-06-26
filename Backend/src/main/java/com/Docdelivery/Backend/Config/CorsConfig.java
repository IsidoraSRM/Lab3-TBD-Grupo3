package com.Docdelivery.Backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Permitir todas las URL
        configuration.addAllowedMethod("*");       // Permitir todos los métodos (GET, POST, etc.)
        configuration.addAllowedHeader("*");       // Permitir todos los encabezados
        configuration.setAllowCredentials(true);   // Permitir credenciales como cookies o cabeceras de autenticación

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}
