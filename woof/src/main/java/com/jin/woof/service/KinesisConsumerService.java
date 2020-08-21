package com.jin.woof.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.*;
import software.amazon.awssdk.services.kinesis.model.ListShardsRequest;
import software.amazon.awssdk.utils.Pair;
import software.amazon.awssdk.utils.StringUtils;

import java.util.List;

@Slf4j
@Service
public class KinesisConsumerService {

    private static final int MAX_NUMBER_OF_SHARDS_SIZE = 1;
    private final KinesisClient kinesisClient;

    @Autowired
    public KinesisConsumerService(KinesisClient kinesisClient) {
        this.kinesisClient = kinesisClient;
    }

    public Pair<String, List<Record>> pollForRecords(String streamName, String shardIterator) {
        String iterator = getListOfShards(streamName, shardIterator);

        return getRecords(iterator);
    }

    public Pair<String, List<Record>> getRecords(String shardIterator) {
        GetRecordsRequest getRecordsRequest = GetRecordsRequest
            .builder()
            .shardIterator(shardIterator)
            .limit(100)
            .build();

        GetRecordsResponse response = kinesisClient.getRecords(getRecordsRequest);
        List<Record> records = response.records();
        String nextShardIterator = response.nextShardIterator();

        return Pair.of(nextShardIterator, records);
    }

    public String getListOfShards(String streamName, String shardIterator) {
        String iterator = shardIterator;
        ListShardsRequest listShardsRequest = ListShardsRequest
            .builder()
            .streamName(streamName)
            .build();

        List<Shard> shards = kinesisClient.listShards(listShardsRequest).shards();

        if (shards.size() < MAX_NUMBER_OF_SHARDS_SIZE) {
            return "";
        }

        if (StringUtils.isBlank(iterator)) {
            iterator = getShardIterator(streamName, shards);
        }

        return iterator;
    }

    // Pick up where we left off since the last time we called
    public String getShardIterator(String streamName, List<Shard> shards) {
        GetShardIteratorRequest getShardIteratorRequest = GetShardIteratorRequest.builder()
            .streamName(streamName)
            .shardId(shards.get(0).shardId())
            .shardIteratorType("LATEST")
            .build();
        GetShardIteratorResponse getShardIteratorResult = kinesisClient.getShardIterator(getShardIteratorRequest);
        String shardIterator = getShardIteratorResult.shardIterator();

        return shardIterator;
    }

}
