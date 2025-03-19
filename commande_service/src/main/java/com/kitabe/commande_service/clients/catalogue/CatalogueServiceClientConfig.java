package com.kitabe.commande_service.clients.catalogue;

import com.kitabe.commande_service.ApplicationProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "rest.client")
public class CatalogueServiceClientConfig {
    private int connectTimeOut;
    private int readTimeOut;

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    private static final Logger logger = LoggerFactory.getLogger(CatalogueServiceClientConfig.class);
    @Bean
    RestClient restClient(ApplicationProperties properties) {
        String baseurl = properties.catalogueService_url();
        if(baseurl == null || baseurl.trim().isEmpty()){
            logger.error("Catalogue service URL is not set");
            throw new IllegalStateException("Catalogue service URL is not set");
        }
        logger.info("Catalogue service URL: " + baseurl);
        // Créez un SimpleClientHttpRequestFactory avec les timeouts configurés
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis()); // Timeout de connexion : 5 secondes
        factory.setReadTimeout((int) Duration.ofSeconds(5).toMillis());    // Timeout de lecture : 5 secondes

        // Créez le RestClient avec le RequestFactory configuré
        return RestClient.builder()
                .baseUrl(baseurl)
                .requestFactory(factory) // Appliquez le RequestFactory personnalisé
                .build();
    }
}
