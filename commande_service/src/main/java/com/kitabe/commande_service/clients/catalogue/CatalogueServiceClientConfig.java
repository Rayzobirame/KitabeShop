package com.kitabe.commande_service.clients.catalogue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitabe.commande_service.ApplicationProperties;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
        // Configurer les timeouts
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofDays(Duration.ofSeconds(5).toMillisPart()))
                .setResponseTimeout(Timeout.ofDays(Duration.ofSeconds(5).toMillisPart()))
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClients.custom().setDefaultRequestConfig(requestConfig).build()
        );

        // Créez le RestClient avec le RequestFactory configuré
        return RestClient.builder()
                .baseUrl(baseurl)
                .requestFactory(factory)
                .build();
    }
}
