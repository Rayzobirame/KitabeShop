package com.kitabe.commande_service.clients.catalogue;

import com.kitabe.commande_service.ApplicationProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class CatalogueServiceClientConfig {
    private static final Logger logger = LoggerFactory.getLogger(CatalogueServiceClientConfig.class);
    @Bean
    RestClient restClient(ApplicationProperties properties) {
        String baseurl = properties.catalogueService_url();
        if(baseurl == null || baseurl.trim().isEmpty()){
            logger.error("Catalogue service URL is not set");
            throw new IllegalStateException("Catalogue service URL is not set");
        }
        logger.info("Catalogue service URL: " + baseurl);

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(5).toMillis());
        return RestClient.builder().baseUrl(baseurl).requestFactory(factory).build();
    }
}
