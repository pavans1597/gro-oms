package com.groyyo.order.management.config;

import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.groyyo.core.base.constants.InterceptorConstants;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.HeaderParameter;

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

	@Bean
	public GlobalOpenApiCustomizer customizer() {
		return openApi -> openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
				.forEach(operation -> operation.addParametersItem(new HeaderParameter().name(InterceptorConstants.HEADER_FACTORY_ID_NAME)
						.description(InterceptorConstants.HEADER_FACTORY_ID_DESCRIPTION)
						.in(ParameterIn.HEADER.toString())
						.required(Boolean.FALSE)));
	}

}