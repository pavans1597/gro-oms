package com.groyyo.order.management.config;

//import com.groyyo.order.management.filter.CORSFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

  @Value("${issuerUri}")
  private String issuerUri;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(c -> {
      CorsConfiguration corsConfiguration = new CorsConfiguration();
      corsConfiguration.setAllowedOrigins(List.of("*"));
      corsConfiguration.setAllowedMethods(List.of("*"));
      corsConfiguration.setAllowedHeaders(List.of("*"));
      corsConfiguration.setAllowCredentials(false);

      UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
      configurationSource.registerCorsConfiguration("/**", corsConfiguration);

      c.configurationSource(configurationSource);
    });

    return http
            .oauth2ResourceServer(
                    j -> j.jwt().jwkSetUri(issuerUri)
            ).authorizeRequests()
            .mvcMatchers(
                    "/swagger-resources/**",
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
                    "/webjars/**")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            //.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
            .build();
  }
}

// http://localhost:8080/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://localhost:3000/authorized&code_challenge=QYPAZ5NU8yvtlQ9erXrUYR-T5AGCjCF47vN-KsaI2A8&code_challenge_method=S256
