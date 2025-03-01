package com.kitabe.catalogue_service;

import org.springframework.boot.SpringApplication;

public class TestCatalogueServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(CatalogueServiceApplication::main)
                .with(ConteneurConfiguration.class)
                .run(args);
    }
}
