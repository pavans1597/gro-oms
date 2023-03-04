package com.groyyo.order.management.config;

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
		return new OpenAPI().info(apiInfo());
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

}