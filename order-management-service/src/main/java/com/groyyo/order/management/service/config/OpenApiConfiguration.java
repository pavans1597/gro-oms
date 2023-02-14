package com.groyyo.quality.management.service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;


@Configuration
//@SecurityScheme(name = "bearerAuth",
//		type = SecuritySchemeType.HTTP,
//		bearerFormat = "JWT",
//		scheme = "bearer")
public class OpenApiConfiguration {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(apiInfo());
	}

	private Info apiInfo() {
		return new Info().title("Bootstrap Postgres Service API's")
				.description("API's for Generic Bootstrap Postgres Service").version("2.0").contact(apiContact())
				.license(apiLicence());
	}

	private License apiLicence() {
		return new License();
	}

	private Contact apiContact() {
		return new Contact().name("Pavan Kumar").email("pavan@groyyo.com").url("https://github.com/pavan1597");
	}
}
