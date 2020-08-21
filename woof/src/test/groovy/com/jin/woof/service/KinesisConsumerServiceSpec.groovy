package com.jin.woof.service

import software.amazon.awssdk.services.kinesis.KinesisClient
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest
import software.amazon.awssdk.services.kinesis.model.GetRecordsResponse
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorRequest
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorResponse
import software.amazon.awssdk.services.kinesis.model.ListShardsRequest
import software.amazon.awssdk.services.kinesis.model.ListShardsResponse
import software.amazon.awssdk.services.kinesis.model.Record
import software.amazon.awssdk.services.kinesis.model.Shard
import software.amazon.awssdk.utils.Pair
import spock.lang.Specification

class KinesisConsumerServiceSpec extends Specification {
    KinesisClient kinesisClient
    KinesisConsumerService kinesisConsumerService

    void setup() {
        kinesisClient = Mock(KinesisClient)
        kinesisConsumerService = new KinesisConsumerService(kinesisClient)
        0 * _
    }

    void 'pollForRecords'() {
        given:
        String streamName = "name"
        String shardIterator = "shardIterator-1"
        List<Shard> shardsList = [Shard.builder().build(), Shard.builder().build()]
        ListShardsResponse listShardsResponse = ListShardsResponse.builder()
            .shards(shardsList)
            .build()
        GetRecordsResponse response = GetRecordsResponse.builder()
            .records([])
            .nextShardIterator("nextShardIterator")
            .build()

        when:
        Pair<String, List<Record>> result = kinesisConsumerService.pollForRecords(streamName, shardIterator)

        then:
        1 * kinesisClient.listShards(
            ListShardsRequest.builder()
                .streamName(streamName)
                .build()
        ) >> listShardsResponse

        1 * kinesisClient.getRecords(
            GetRecordsRequest.builder()
                .shardIterator(shardIterator)
                .limit(100)
                .build()
        ) >> response

        result != null
    }

    void 'getRecords'() {
        given:
        String shardIterator = "shardIterator-1"
        GetRecordsResponse response = GetRecordsResponse.builder()
            .records([])
            .nextShardIterator("nextShardIterator")
            .build()

        when:
        Pair<String, List<Record>> result = kinesisConsumerService.getRecords(shardIterator)

        then:
        1 * kinesisClient.getRecords(
            GetRecordsRequest.builder()
                .shardIterator(shardIterator)
                .limit(100)
                .build()
        ) >> response

        and:
        result.left() == "nextShardIterator"
        result.right() == []
    }

    void 'getListOfShards'() {
        given:
        String streamName = "name"
        String shardIterator = "shardIterator-1"
        List<Shard> shardsList = [Shard.builder().shardId("id")]
        ListShardsResponse listShardsResponse = ListShardsResponse.builder()
            .shards(shardsList)
            .build()

        when:
        String result = kinesisConsumerService.getListOfShards(streamName, shardIterator)

        then:
        1 * kinesisClient.listShards(
            ListShardsRequest.builder()
                .streamName(streamName)
                .build()
        ) >> listShardsResponse

        and:
        result != null
    }

    void 'getShardIterator'() {
        given:
        String streamName = "name"
        List<Shard> shardsList = [Shard.builder().build(), Shard.builder().build()]
        GetShardIteratorResponse getShardIteratorResult = GetShardIteratorResponse.builder()
                .shardIterator("shardIterator-1")
                .build()

        when:
        String shardIterator = kinesisConsumerService.getShardIterator(streamName, shardsList)

        then:
        1 * kinesisClient.getShardIterator(
            GetShardIteratorRequest.builder()
                .streamName(streamName)
                .shardId(shardsList.get(0).shardId())
                .shardIteratorType("LATEST")
                .build()
        ) >> getShardIteratorResult

        and:
        shardIterator == getShardIteratorResult.shardIterator();
    }
}
