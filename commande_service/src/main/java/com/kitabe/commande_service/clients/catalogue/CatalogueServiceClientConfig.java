package com.kitabe.commande_service.clients.catalogue;

import com.kitabe.commande_service.ApplicationProperties;
import java.time.Duration;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Classe de configuration pour la mise en place du client REST utilisé pour communiquer avec le service Catalogue.
 * Cette classe fournit une définition de bean pour un {@link RestClient} configuré avec des délais d'attente et une URL de base
 * récupérée à partir des propriétés de l'application. Elle utilise les {@link ConfigurationProperties} de Spring pour lier
 * les paramètres de configuration et inclut une journalisation pour les besoins de débogage.
 */
@Configuration
@ConfigurationProperties(prefix = "rest.client")
public class CatalogueServiceClientConfig {
    private int connectTimeOut;
    private int readTimeOut;

    /**
     * Récupère la valeur du délai d'attente de connexion en millisecondes.
     *
     * @return la durée du délai d'attente de connexion
     */
    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    /**
     * Définit la valeur du délai d'attente de connexion en millisecondes.
     *
     * @param connectTimeOut la durée du délai d'attente de connexion à définir
     */
    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    /**
     * Récupère la valeur du délai d'attente de lecture en millisecondes.
     *
     * @return la durée du délai d'attente de lecture
     */
    public int getReadTimeOut() {
        return readTimeOut;
    }

    /**
     * Définit la valeur du délai d'attente de lecture en millisecondes.
     *
     * @param readTimeOut la durée du délai d'attente de lecture à définir
     */
    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }


    private static final Logger logger = LoggerFactory.getLogger(CatalogueServiceClientConfig.class);

    /**
     * Crée et configure un bean {@link RestClient} pour interagir avec le service Catalogue.
     * Le client est configuré avec une URL de base provenant des propriétés de l'application et des paramètres de délai d'attente personnalisés.
     * Si l'URL du service Catalogue n'est pas définie, une {@link IllegalStateException} est levée.
     *
     * @param builder le RestClient.Builder pour construire le client
     * @param properties les propriétés de l'application contenant l'URL du service Catalogue
     * @return une instance configurée de RestClient
     * @throws IllegalStateException si l'URL du service Catalogue n'est pas fournie
     */
    @Bean
    RestClient restClient(RestClient.Builder builder,ApplicationProperties properties) {
        String baseurl = properties.catalogueService_url();
        if (baseurl == null || baseurl.trim().isEmpty()) {
            logger.error("Catalogue service URL is not set");
            throw new IllegalStateException("Catalogue service URL is not set");
        }
        logger.info("Catalogue service URL: " + baseurl);
        // Configuration des délais d'attente
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofDays(Duration.ofSeconds(5).toMillisPart()))
                .setResponseTimeout(Timeout.ofDays(Duration.ofSeconds(5).toMillisPart()))
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClients.custom().setDefaultRequestConfig(requestConfig).build());
        // Créez le RestClient avec le RequestFactory configuré
        return builder.baseUrl(baseurl).requestFactory(factory).build();
    }
}
