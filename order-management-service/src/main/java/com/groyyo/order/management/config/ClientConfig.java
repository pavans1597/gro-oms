package com.groyyo.order.management.config;

import com.groyyo.core.base.http.GroyyoRestClient;
import com.groyyo.core.masterData.client.api.MasterDataApi;
import com.groyyo.core.masterData.client.cache.MasterDataCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ClientConfig {

    @Value("${service.masterData.url}")
    private String masterDataServiceUrl;

    @Bean(name = "masterDataClient")
    public GroyyoRestClient masterDataClient() {
        return new GroyyoRestClient(masterDataServiceUrl);
    }

    @Bean
    public MasterDataApi masterDataApi() {
        return new MasterDataApi(masterDataClient());
    }

    @Bean
    public MasterDataCache masterDataCache() {
        return new MasterDataCache(masterDataApi());
    }

}
