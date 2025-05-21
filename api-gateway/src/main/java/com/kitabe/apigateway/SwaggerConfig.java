package com.kitabe.apigateway;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

/**
 * Classe de configuration pour Swagger UI dans l'API Gateway.
 * Génère dynamiquement les URLs Swagger UI pour chaque microservice en fonction des définitions de routes de l'API Gateway.
 * Cela permet à Swagger UI de regrouper la documentation des API de tous les microservices.
 */
@Configuration
public class SwaggerConfig {

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;
    private final RouteDefinitionLocator routeDefinitionLocator;

    /**
     * Constructeur de SwaggerConfig.
     *
     * @param swaggerUiConfigProperties Propriétés pour configurer Swagger UI.
     * @param routeDefinitionLocator Localisateur pour récupérer les définitions de routes de l'API Gateway.
     */
    public SwaggerConfig(
            SwaggerUiConfigProperties swaggerUiConfigProperties, RouteDefinitionLocator routeDefinitionLocator) {
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    /**
     * Initialise les URLs Swagger UI après la construction du bean.
     * Récupère toutes les définitions de routes de l'API Gateway, filtre celles qui se terminent par "-service",
     * et génère une URL Swagger pour chaque microservice.
     */
    @PostConstruct
    public void init() {
        // Ensemble pour stocker les URLs Swagger des microservices
        Set<SwaggerUrl> urls = new HashSet<>();

        // Récupère les définitions de routes de manière réactive et les traite
        Flux<RouteDefinition> routeDefinitions = routeDefinitionLocator.getRouteDefinitions();
        routeDefinitions
                .filter(routeDefinition ->
                        routeDefinition.getId().endsWith("-service")) // Filtre les routes des microservices
                .subscribe(routeDefinition -> {
                    // Extrait le nom du service en supprimant le suffixe "-service"
                    String name = routeDefinition.getId().replace("-service", "");
                    // Crée une URL Swagger pour le microservice (ex. /v3/api-docs/catalogue)
                    SwaggerUrl swaggerUrl = new SwaggerUrl(name, DEFAULT_API_DOCS_URL + "/" + name, null);
                    // Ajoute l'URL à l'ensemble
                    urls.add(swaggerUrl);
                });

        // Définit les URLs Swagger dans les propriétés de configuration
        swaggerUiConfigProperties.setUrls(urls);
    }
}
