package com.uade.tpo.marketplace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI marketplaceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Marketplace Camisetas API")
                        .version("1.0.0")
                        .description("Documentacion de endpoints REST para el marketplace de camisetas del Mundial 2026."));
    }
}
