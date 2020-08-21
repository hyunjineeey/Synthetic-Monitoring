package com.jin.goalie.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConfigurationProperties("goalie")
@EnableScheduling
public class GoalieConfig {
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
