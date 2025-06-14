package com.kitabe.kitabe_webapp.clients;

import com.kitabe.kitabe_webapp.ApplicationProperties;
import com.kitabe.kitabe_webapp.catalogue.CatalogueServiceClient;
import com.kitabe.kitabe_webapp.commande.CommandeServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {
    private final ApplicationProperties applicationProperties;

    public ClientConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    CatalogueServiceClient catalogueServiceClient() {
        RestClient restClient = RestClient.create(applicationProperties.apiGatewayUrl());
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(CatalogueServiceClient.class);
    }

    @Bean
    CommandeServiceClient commandeServiceClient() {
        RestClient restClient = RestClient.create(applicationProperties.apiGatewayUrl());
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        return factory.createClient(CommandeServiceClient.class);
    }


}
