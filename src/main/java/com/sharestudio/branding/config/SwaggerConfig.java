package com.sharestudio.branding.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	private static final String SWAGGER_API_VERSION = "1.0";
    private static final String LICENSE_TEXT = "License of API";
    private static final String TITLE = "Sharestudio branding Service API";
    private static final String DESCRIPTION = "These are the branding api for sharestudio.";
    private static final String LICENSE_URL = "These are the branding api for sharestudio.";

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(TITLE)
                        .description(DESCRIPTION)
                        .version(SWAGGER_API_VERSION)
                        .license(new License().name(LICENSE_TEXT).url(LICENSE_URL)))
                .externalDocs(new ExternalDocumentation());
    }
}
