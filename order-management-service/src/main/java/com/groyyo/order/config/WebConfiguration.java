package com.groyyo.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.groyyo.core.multitenancy.interceptor.TenantInterceptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	private final TenantInterceptor tenantInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addWebRequestInterceptor(tenantInterceptor);
	}

}