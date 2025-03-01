package com.kitabe.catalogue_service.web.Controlleurs;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.kitabe.catalogue_service.AbstractIT;
import com.kitabe.catalogue_service.domaine.Produit;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/data-test.sql")
class ProduitsControllerTest extends AbstractIT {

    @Test
    void shouldRetourProduit() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/produits")
                .then()
                .statusCode(200)
                .body("data", hasSize(10))
                .body("totalElements", is(24))
                .body("pageNumber", is(1))
                .body("totalPages", is(3))
                .body("isFirst", is(true))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrevious", is(false));
    }

    @Test
    void shouldGetProduitByCode() {
        Produit produit = given().contentType(ContentType.JSON)
                .when()
                .get("/api/produits/{code}", "LIV-FAN-002")
                .then()
                .statusCode(200)
                .assertThat()
                .extract()
                .body()
                .as(Produit.class);

        assertThat(produit.code()).isEqualTo("LIV-FAN-002");
        assertThat(produit.nom()).isEqualTo("Le Seigneur des Anneaux: La Communauté de l'Anneau");
        assertThat(produit.description())
                .isEqualTo(
                        "Premier tome de la trilogie de J.R.R. Tolkien, une épopée fantastique dans un monde imaginaire.");
        assertThat(produit.prix()).isEqualTo(new BigDecimal("16.50"));
    }

    @Test
    void shouldRetourneNotFoundWhenProduitCodeNotExiste() {
        String code = "code_produit_introuvable";
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/produits/{code}", code)
                .then()
                .statusCode(404)
                .body("status", is(404))
                .body("title", is("Produit non retrouvé"))
                .body("detail", is("Le produit avec le code " + code + " n'existe pas !"));
    }
}
