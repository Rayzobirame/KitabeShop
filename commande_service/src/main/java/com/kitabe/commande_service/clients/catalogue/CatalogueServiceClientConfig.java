package com.kitabe.commande_service.clients.catalogue;

import com.kitabe.commande_service.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CatalogueServiceClientConfig {

    @Bean
    RestClient restClient(ApplicationProperties properties) {
        return RestClient.builder().baseUrl(properties.catalogueService_url()).build();
    }
}
