package com.jin.woof.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConfigurationProperties("woof")
@EnableScheduling
public class WoofConfig {
    @Getter
    @Setter
    public String baseUrl;

    @Getter
    @Setter
    public String deviceId;

    @Getter
    @Setter
    public String locationId;
}
