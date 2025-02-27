package com.kitabe.catalogue_service.web.Controlleurs;

import com.kitabe.catalogue_service.AbstractIT;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Sql("/data-test.sql")
class ProduitsControllerTest extends AbstractIT {

    @Test
    void shouldRetourProduit(){
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/produits")
                .then()
                .statusCode(200)
                .body("data",hasSize(10))
                .body("totalElements",is(24))
                .body("pageNumber", is(1))
                .body("totalPages",is(3))
                .body("isFirst",is(true))
                .body("isLast",is(false))
                .body("hasNext",is(true))
                .body("hasPrevious",is(false));
    }
}