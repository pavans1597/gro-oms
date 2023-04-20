/**
 * 
 */
package com.groyyo.order.management.config;

import com.groyyo.core.multitenancy.multitenancy.interceptor.TenantInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groyyo.core.base.http.interceptor.HeaderInterceptor;
import com.groyyo.core.security.SecurityModuleBuilder;
import com.groyyo.core.security.interceptor.AuthInterceptor;
import com.groyyo.core.security.interceptor.UIDInterceptor;

/**
 * @author pavan
 *
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${enable.cors:false}")
	private Boolean enableCors;

	@Value("${enable.authentication:false}")
	private Boolean enableAuthentication;

	@Value("${enable.authorize:false}")
	private Boolean enableAuthorization;

	@Value("${service.user.url}")
	private String userServiceUrl;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		AntPathMatcher matcher = new AntPathMatcher();
		matcher.setCaseSensitive(false);
		configurer.setPathMatcher(matcher);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("GET", "PUT", "POST", "DELETE",
						"PATCH", "OPTIONS", "HEAD").allowedHeaders("*");
			}
		};
	}

	@Bean
	public HeaderInterceptor headerInterceptor() {
		return new HeaderInterceptor();
	}

	@Bean
	public UIDInterceptor uidInterceptor() {
		return new UIDInterceptor();
	}

	@Bean
	public AuthInterceptor authInterceptor() {

		return new SecurityModuleBuilder()
				.objectMapper(objectMapper)
				.corsSupport(enableCors)
				.userServiceUrl(userServiceUrl)
				.enableTokenBasedAuthentication(enableAuthentication)
				.enableUrlBasedAuthorization(enableAuthorization)
				.build();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(uidInterceptor())
				.addPathPatterns("/**").order(Ordered.HIGHEST_PRECEDENCE);

		registry.addInterceptor(authInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/**")
				.excludePathPatterns(
						"/internal/**",
						"/ping",
						"/kafka/**",
						"/v2/api-docs",
						"/v3/api-docs",
						"/v3/api-docs/**",
						"/configuration/ui",
						"/swagger-resources/**",
						"/configuration/security",
						"/swagger-ui.html",
						"/swagger-ui/index.html",
						"/swagger-ui/**",
						"/webjars/**");

		registry.addInterceptor(headerInterceptor());
	}
}