package com.kitabe.catalogue_service;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.integration.annotation.Default;

@ConfigurationProperties(prefix = "catalogue")
public record ApplicationProperties (
        @DefaultValue("10")
        @Min(1)
        int pageSize){

}
