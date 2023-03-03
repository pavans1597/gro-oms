/**
 * 
 */
package com.groyyo.order.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}
}