package com.deakin.opsnexus.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI opsnexusOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OpsNexus Enterprise Workflow API")
                        .description("Cloud-Native DevOps Task Orchestration REST API with Automated 7-Stage Jenkins CI/CD, Prometheus Observability, and JWT RBAC Security.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DevOps Engineering Team")
                                .email("devops@deakin.edu.au")
                                .url("https://github.com/deakin/opsnexus-api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local / Production Environment"),
                        new Server().url("http://localhost:8081").description("Staging Environment")
                ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter your JWT Bearer token obtained from POST /api/auth/login")));
    }
}
