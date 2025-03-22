package com.kitabe.commande_service.clients.catalogue;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ProduitServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(ProduitServiceClient.class);
    private final RestClient restClient;

    public ProduitServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @CircuitBreaker(name = "catalogue_service")
    @Retry(name="catalogue_service",fallbackMethod = "getProduitByCodeFallBack")
    public Optional<Produit> getProduitByCode(String code) {
        logger.info("Fetching produit by code: {}", code);
            Produit produit = restClient.get()
                    .uri("/api/produits/{code}", code)
                    .retrieve()
                    .body(Produit.class);
            logger.info("Successfully fetched produit: {}", produit);
            return Optional.ofNullable(produit);

    }

    public Optional<Produit> getProduitByCodeFallBack(String code, Throwable t) {
        logger.error("Fallback triggered for getProduitByCode with code {}: {}", code, t.getMessage(), t);
        return Optional.empty();
    }
}
