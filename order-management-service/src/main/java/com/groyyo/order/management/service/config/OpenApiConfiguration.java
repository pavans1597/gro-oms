package com.groyyo.order.management.service.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo()).components(components());
    }

    private Info apiInfo() {
        return new Info()
                .title("Order Management Service APIs")
                .description("APIs for Order Management Service")
                .version("2.0")
                .contact(apiContact())
                .license(apiLicence());
    }

    private License apiLicence() {
        return new License();
    }

    private Contact apiContact() {
        return new Contact()
                .name("Gagan Soni")
                .email("gagansoni@groyyo.com")
                .url("https://www.groyyo.com");
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes("ApiKeyHeader", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("X-TENANT-ID"));
    }

    @Bean
    public GlobalOpenApiCustomizer customizer() {
        return openApi -> openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
                .forEach(operation -> operation.addParametersItem(new HeaderParameter().name("X-TENANT-ID")
                        .description("Tenant Id")
                        .in(ParameterIn.HEADER.toString())
                        .required(false)));
    }
}