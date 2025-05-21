package com.kitabe.commande_service;

import org.springframework.boot.SpringApplication;

public class TestCommandeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(CommandeServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
