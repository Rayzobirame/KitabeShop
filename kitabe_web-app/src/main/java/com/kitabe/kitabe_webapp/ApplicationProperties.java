package com.kitabe.kitabe_webapp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kitabe")
public record ApplicationProperties (
        String apiGatewayUrl
){}
