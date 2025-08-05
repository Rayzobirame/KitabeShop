package com.kitabe.kitabe_webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Configuration principale de sécurité pour l'application web
 * Définit les règles d'authentification et d'autorisation OAuth2/OIDC
 */
@Configuration // Marque cette classe comme source de configuration Spring
@EnableWebSecurity // Active la sécurité web Spring Security
public class SecuriteConfig {

    /**
     * Repository contenant les configurations des clients OAuth2
     * Configuré via application.properties (client-id, client-secret, etc.)
     */
    private final ClientRegistrationRepository registrationRepository;

    public SecuriteConfig(ClientRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    /**
     * Configure la chaîne de filtres de sécurité
     * Remplace l'ancienne approche WebSecurityConfigurerAdapter
     *
     * @param http Configuration de sécurité HTTP
     * @return SecurityFilterChain La chaîne de filtres configurée
     * @throws Exception En cas d'erreur de configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> c.requestMatchers(
                                "/js/**",
                                "/css/**",
                                "/images/**",
                                "/error",
                                "/webjars/**",
                                "/api/token",
                                "/",
                                "/actuator/**",
                                "/produits/**",

                                "/api/produits/**"
                        ).permitAll()           // Autorise l'accès sans authentification
                        .anyRequest()       // Toute autre requête
                        .authenticated())   // Nécessite une authentification

                // Désactive CORS (Cross-Origin Resource Sharing)
                // AbstractHttpConfigurer::disable utilise une référence de méthode
                .cors(AbstractHttpConfigurer::disable)
                // Désactive CSRF (Cross-Site Request Forgery)
                // Généralement désactivé pour les API REST et OAuth2
                .csrf(AbstractHttpConfigurer::disable)
                // Active le client OAuth2 avec configuration par défaut
                // Permet à l'application d'agir comme client OAuth2
                .oauth2Client(Customizer.withDefaults())
                // Active la connexion OAuth2/OIDC
                // Ajoute les endpoints /oauth2/authorization/* et /login/oauth2/code/*
                .oauth2Login(Customizer.withDefaults())

                // Configuration de la déconnexion
                .logout(logout -> logout
                        .clearAuthentication(true)      // Efface l'authentification du SecurityContext
                        .invalidateHttpSession(true)    // Invalide la session HTTP
                        .logoutSuccessHandler(oidcLogoutSuccessHandler()) // Gestionnaire de déconnexion OIDC
                );

        return http.build(); // Construit la chaîne de filtres
    }
    /**
     * Crée un gestionnaire de déconnexion OIDC
     * Permet de déconnecter l'utilisateur du serveur d'autorisation (Keycloak)
     *
     * @return LogoutSuccessHandler Gestionnaire de déconnexion configuré
     */

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        // Crée un gestionnaire de déconnexion initié par le client OIDC
        // Redirige vers l'endpoint de déconnexion du serveur d'autorisation
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.registrationRepository);
        // Configure l'URI de redirection après déconnexion
        // {baseUrl} sera remplacé par l'URL de base de l'application
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
        return oidcLogoutSuccessHandler;
    }
}