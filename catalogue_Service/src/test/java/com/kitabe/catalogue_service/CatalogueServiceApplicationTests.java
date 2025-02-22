package com.kitabe.catalogue_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(ConteneurConfiguration.class)
@SpringBootTest
class CatalogueServiceApplicationTests {

    @Test
    void contextLoads() {}
}
