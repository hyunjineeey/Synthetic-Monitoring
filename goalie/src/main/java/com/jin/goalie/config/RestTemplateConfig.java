package com.jin.goalie.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private final RestTemplateBuilder builder;

    public RestTemplateConfig(RestTemplateBuilder restTemplateBuilder) {
        this.builder = restTemplateBuilder;
    }

    @Bean
    public RestTemplate goalieRestTemplate() {
        return builder
            .additionalMessageConverters(new ProtobufHttpMessageConverter())
            .build();
    }



}
