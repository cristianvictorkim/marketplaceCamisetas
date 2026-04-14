package com.uade.tpo.marketplace.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String JWT_SECURITY_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI marketplaceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Marketplace Camisetas API")
                        .version("1.0.0")
                        .description("Documentacion de endpoints REST para el marketplace de camisetas del Mundial 2026."))
                .addSecurityItem(new SecurityRequirement().addList(JWT_SECURITY_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(JWT_SECURITY_SCHEME, new SecurityScheme()
                                .name(JWT_SECURITY_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
