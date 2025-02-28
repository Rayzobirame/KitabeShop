package com.kitabe.catalogue_service.domaine;

import com.kitabe.catalogue_service.ConteneurConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Ce classe est un prototype exemplaire montrant commment on test un repository sur un microservice avec les testcontainer

@DataJpaTest(
        properties = {
                "spring.test.database.replace=none",
                "spring.datasource.url= jdbc:tc:postgresql:16-alpine:///test?TC_DAEMON=true"
                //"spring.datasource.url= jdbc:tc:postgresql:16-alpine:///db"  aussi une possiblite
        }
)

//@Import(ConteneurConfiguration.class)
@Sql("/data-test.sql")
public class ProduitRepositoryTest {
    @Autowired private ProduitRepository produitRepository;

    // cette methode teste la recuperation des 24 elements enregistrer sur le fichier data-test.sql
    @Test
    public void shouldRetournerProduit() {
        List<ProduitEntite> produits = produitRepository.findAll();
        assertThat(produits).hasSize(24);
    }

    @Test
    void shouldRetourneProduitByCode(){
        ProduitEntite produit = produitRepository.findByCode("LIV-FAN-002").orElseThrow();
        assertThat(produit.getCode()).isEqualTo("LIV-FAN-002");
        assertThat(produit.getNom()).isEqualTo("Le Seigneur des Anneaux: La Communauté de l'Anneau");
        assertThat(produit.getDescription()).isEqualTo("Premier tome de la trilogie de J.R.R. Tolkien, une épopée fantastique dans un monde imaginaire.");
        assertThat(produit.getPrix()).isEqualTo(new BigDecimal("16.50"));
    }

    @Test
    void shouldRetournEmptyWhenProduitCodeExistePas(){
        assertThat(produitRepository.findByCode("code_produit_introuvable")).isEmpty();
    }
}