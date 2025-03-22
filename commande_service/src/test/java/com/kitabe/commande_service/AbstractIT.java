package com.kitabe.commande_service;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.MediaType;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIT {
    @LocalServerPort
    int port;

    static WireMockContainer wireMockServer = new WireMockContainer("wiremock/wiremock:3.5.2-alpine");



    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("commande.catalogueService_url", wireMockServer::getBaseUrl);
    }

    @BeforeAll
    static void beforeAll() {
        wireMockServer.start();
        configureFor(wireMockServer.getHost(), wireMockServer.getPort());
    }

    protected static void mockGetProduitByCode(String code, String nom, BigDecimal prix) {
        stubFor(WireMock.get(urlMatching("/api/produits/" + code))
                        .willReturn(aResponse()
                        .withHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(
                                """
                                  {
                                      "code": "%s",
                                      "nom": "%s",
                                      "prix": "%s"
                                  }
                                 """.formatted(code, nom, prix.toString()))));
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
}
