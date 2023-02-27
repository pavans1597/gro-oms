package com.groyyo.order.management.service.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.groyyo.core.base.localdate.Java8LocalDateStdDeserializer;
import com.groyyo.core.base.localdate.Java8LocalDateStdSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.LocalDate;

@Configuration
@ConfigurationProperties
@RequiredArgsConstructor
public class AppConfig {

    @Autowired
    private Environment environment;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        SimpleModule module = new SimpleModule();
        module.addSerializer(new Java8LocalDateStdSerializer());
        module.addDeserializer(LocalDate.class, new Java8LocalDateStdDeserializer());

        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }


    @Bean("mediaContainerBaseUrl")
    String provideGroyyoMediaContainerBaseUrl() {
        String container = environment.getProperty("ngroyyo.media.container");
        String accountName = environment.getProperty("groyyo.media.accountName");
        String endpointSuffix = environment.getProperty("groyyo.media.endpointSuffix");
        return "https://" + accountName + ".blob." + endpointSuffix + "/" + container;
    }


    @Bean
    BlobContainerClient provideGroyyoMediaBlobContainer() {
        String container = environment.getProperty("groyyo.media.container");
        String connectionString = azConnectionString();
        return new BlobServiceClientBuilder().connectionString(connectionString).buildClient().getBlobContainerClient(container);
    }

    private String azConnectionString() {
        String accountKey = environment.getProperty("groyyo.media.accountKey");
        String accountName = environment.getProperty("groyyo.media.accountName");
        String endpointSuffix = environment.getProperty("groyyo.media.endpointSuffix");
        return "DefaultEndpointsProtocol=https;AccountName=" + accountName + ";AccountKey=" + accountKey + ";EndpointSuffix=" + endpointSuffix;
    }

    @Bean
    BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder().connectionString(azConnectionString()).buildClient();
    }


}
