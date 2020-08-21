package com.jin.goalie.service;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.CreateStreamRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import java.util.List;

@Slf4j
@Service
public class EventService {

    private final KinesisClient kinesisClient;
    private final String streamName = "my-stream";

    @Autowired
    public EventService(KinesisClient kinesisClient) {
        this.kinesisClient = kinesisClient;
    }

    public void sendSomeMessage(byte[] event) {
        createStreamIfNotExists(streamName);
        PutRecordRequest putRecordsRequest  = PutRecordRequest.builder()
            .data(SdkBytes.fromByteArray(event))
            .partitionKey("partitionKey")
            .streamName(streamName)
            .build();

        kinesisClient.putRecord(putRecordsRequest);
    }

    @SneakyThrows
    public void createStreamIfNotExists(String streamName) {
        List<String> streamsInKinesis = kinesisClient.listStreams().streamNames();
        if (streamsInKinesis.contains(streamName)) {
            return;
        } else {
            log.info("Doesn't exist. Creating now! Waiting to return. For 2 seconds");
            Thread.sleep(2000L);
            CreateStreamRequest createStreamRequest = CreateStreamRequest.builder()
                .streamName(streamName)
                .shardCount(1)
                .build();
            kinesisClient.createStream(createStreamRequest);
        }
    }



}
