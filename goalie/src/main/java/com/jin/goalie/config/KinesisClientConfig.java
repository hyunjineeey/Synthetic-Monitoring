package com.jin.goalie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.KinesisClientBuilder;

import java.net.URI;

@Configuration
public class KinesisClientConfig {

    @Bean
    public KinesisClient kinesisClient() {
        KinesisClientBuilder clientBuilder = KinesisClient.builder();

        // Saves from exporting environment variables
        AwsSessionCredentials awsCreds = AwsSessionCredentials.create(
            "your_access_key_id_here",
            "your_secret_key_id_here",
            "your_session_token_here");

        System.setProperty("org.apache.flink.kinesis.shaded.com.amazonaws.sdk.disableCbor", "true");
        System.setProperty("aws.cborEnabled", "false");

        clientBuilder.region(Region.AP_EAST_1);

        clientBuilder.credentialsProvider(StaticCredentialsProvider.create(awsCreds));
        clientBuilder.endpointOverride(URI.create("http://localhost:4568"));

        return clientBuilder.build();
    }
}
