package com.kitabe.kitabe_webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KitabeWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitabeWebAppApplication.class, args);
    }
}
