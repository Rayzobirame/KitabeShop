package com.kitabe.kitabe_webapp.commande;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;  // Utiliser Spring HttpHeaders au lieu de java.net.http.HttpHeaders
import org.springframework.http.MediaType;    // Utiliser Spring MediaType
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Import(ConteneurConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class AbstractIT {
    static final String CLIENT_ID = "kitabe_web-app";
    static final String CLIENT_SECRET = "ZDeJWdP8e4W4t2e3nxzSWBZwzUmS3ZJq";
    static final String USERNAME = "birame";
    static final String PASSWORD = "camara96";

    @Autowired
    OAuth2ResourceServerProperties properties;

    @LocalServerPort
    int port;

    @Autowired
    protected MockMvc mockMvc;

    static WireMockContainer wireMockServer = new WireMockContainer("wiremock/wiremock:3.5.2");

    @Autowired
    private OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @BeforeAll
    static void beforeAll() {
        wireMockServer.start();
        configureFor(wireMockServer.getHost(), wireMockServer.getPort());
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("commandes.catalogue_service_url", wireMockServer::getBaseUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected static void mockGetProduitByCode(String code, String nom, BigDecimal prix) {
        stubFor(WireMock.get(urlMatching("/api/produits/" + code))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(
                                """
                                {
                                    "code": "%s",
                                    "nom": "%s",
                                    "prix": %f
                                }
                                """.formatted(code, nom, prix.doubleValue())
                        )));
    }

    protected String getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD);
        form.add(OAuth2Constants.CLIENT_ID, CLIENT_ID);
        form.add(OAuth2Constants.CLIENT_SECRET, CLIENT_SECRET);
        form.add(OAuth2Constants.USERNAME, USERNAME);
        form.add(OAuth2Constants.PASSWORD, PASSWORD);

        String auth2serverUrl = oAuth2ResourceServerProperties.getJwt().getIssuerUri() + "/protocol/openid-connect/token";
        var request = new HttpEntity<>(form, httpHeaders);
        KeyCloakToken token = restTemplate.postForObject(auth2serverUrl, request, KeyCloakToken.class);

        assert token != null;
        return token.accessToken();
    }

    record KeyCloakToken(@JsonProperty("access_token") String accessToken) {
    }
}