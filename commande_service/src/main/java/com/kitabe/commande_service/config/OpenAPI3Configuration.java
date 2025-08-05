package com.kitabe.commande_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info; // Import correct pour OpenAPI Info
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server; // Import correct pour OpenAPI Server
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration OpenAPI 3.0 pour le service de commande.
 *
 * Cette classe configure la documentation Swagger/OpenAPI pour le service de commande,
 * incluant la sécurité OAuth2 avec Keycloak et la configuration des serveurs.
 *
 * @author birame
 * @version 1.0.0
 * @since 2024
 */
@Configuration
public class OpenAPI3Configuration {

    /**
     * URI de l'émetteur JWT (serveur Keycloak).
     * Injecté depuis la propriété spring.security.oauth2.resourceserver.jwt.issuer-uri
     */
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    /**
     * URL de l'API Gateway pour la documentation Swagger.
     * Injecté depuis la propriété swagger.api-gateway-url
     */
    @Value("${swagger.api-gateway-url}")
    private String apiGatewayUrl;

    /**
     * Crée et configure l'objet OpenAPI pour la documentation Swagger.
     *
     * Cette méthode configure :
     * - Les informations de base de l'API (titre, description, version, contact)
     * - Le serveur API Gateway comme point d'entrée
     * - La sécurité OAuth2 avec Keycloak (Authorization Code Flow)
     * - Les scopes OpenID Connect
     *
     * @return OpenAPI configuré avec toutes les informations nécessaires
     */
    @Bean
    public OpenAPI getOpenAPI() {
        return new OpenAPI()
                // Configuration des informations de l'API
                .info(new Info()
                        .title("Commande Service API")
                        .description("Kitabe Commande Service API - Gestion des commandes")
                        .version("V1.0.0")
                        .contact(new Contact()
                                .name("birame")
                                .email("birame96@gmail.com")))

                // Configuration du serveur (API Gateway)
                .servers(List.of(new Server()
                        .url(apiGatewayUrl)
                        .description("API Gateway - Point d'entrée principal")))

                // Ajout de l'exigence de sécurité globale
                .addSecurityItem(new SecurityRequirement()
                        .addList("security_auth"))

                // Configuration des composants de sécurité
                .components(new Components()
                        .addSecuritySchemes("security_auth", createOAuth2SecurityScheme()));
    }

    /**
     * Crée le schéma de sécurité OAuth2 pour Keycloak.
     *
     * Configure le flux Authorization Code d'OAuth2 avec :
     * - URL d'autorisation Keycloak
     * - URL de token Keycloak
     * - Scopes OpenID Connect
     *
     * @return SecurityScheme configuré pour OAuth2 avec Keycloak
     */
    private SecurityScheme createOAuth2SecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .in(SecurityScheme.In.HEADER)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(issuerUri + "/protocol/openid-connect/auth")
                                .tokenUrl(issuerUri + "/protocol/openid-connect/token")
                                .scopes(new Scopes()
                                        .addString("openid", "Accès OpenID Connect")
                                        .addString("profile", "Accès au profil utilisateur")
                                        .addString("email", "Accès à l'email utilisateur"))));
    }
}