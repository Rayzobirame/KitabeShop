package com.kitabe.commande_service.clients.catalogue;

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

    public Optional<Produit> getProduitByCode(String code) {
        logger.info("Fetching produit by code: {}" ,code);
       try{
           var produit = restClient.get().uri("/api/produits/{code}", code)
                   .retrieve()
                   .body(Produit.class);
           return Optional.ofNullable(produit);
       } catch (ResourceAccessException ex) {
           logger.error("Timeout or connection error while fetching product with code {}: {}", code, ex.getMessage(), ex);
           return Optional.empty();
       }
       catch (Exception ex) {
           logger.error("Unexpected error while fetching product with code {}: {}", code, ex.getMessage(), ex);
           return Optional.empty();
       }
    }
}
