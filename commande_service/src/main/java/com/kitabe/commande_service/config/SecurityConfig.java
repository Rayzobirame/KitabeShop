package com.kitabe.commande_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuration de la sécurité pour l'application Spring, permettant de définir les règles
 * d'authentification et d'autorisation. Elle utilise Spring Security pour configurer une chaîne de filtres
 * de sécurité basée sur OAuth2 et désactive certaines fonctionnalités comme CORS et CSRF pour un contexte
 * spécifique, tout en imposant une gestion sans état des sessions.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Crée et configure une chaîne de filtres de sécurité ({@link SecurityFilterChain}) pour l'application.
     * Cette méthode configure les endpoints accessibles sans authentification, active l'authentification
     * pour toutes les autres requêtes, utilise une politique de session sans état, désactive CORS et CSRF,
     * et configure le serveur de ressources OAuth2 avec JWT par défaut.
     *
     * @param http l'objet HttpSecurity pour configurer les règles de sécurité
     * @return la chaîne de filtres de sécurité configurée
     * @throws Exception en cas d'erreur lors de la configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> c.requestMatchers(
                "/actuator/**",
                "/v3/api-docs/**"
        ).permitAll()
                .anyRequest()
                .authenticated()).sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(CorsConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
