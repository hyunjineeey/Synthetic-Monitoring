package com.jin.woof.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import jin.event.proto.EventProtos;
import software.amazon.awssdk.services.kinesis.model.Record;
import software.amazon.awssdk.utils.Pair;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@SuppressWarnings("PMD.DoNotUseThreads")
public class SyntheticMonitorService implements Runnable {

    private final KinesisConsumerService kinesisConsumerService;
    private final TimeService timeService;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final TaskExecutor executor;

    @Autowired
    public SyntheticMonitorService(KinesisConsumerService kinesisConsumerService,
                                   TimeService timeService) {
        this.kinesisConsumerService = kinesisConsumerService;
        this.timeService = timeService;
        this.executor = new SimpleAsyncTaskExecutor();
    }

    @PostConstruct
    public void onStart() {
        log.info("starting...");
        running.set(true);
        executor.execute(this);
    }

    @PreDestroy
    public void onShutdown() {
        log.info("stopping...");
        running.set(false);
    }

    @SneakyThrows
    @Override
    public void run() {
        String shardIterator = null;
        Thread.sleep(4700); // Wait for events to start being sent

        while (running.get()) {
            long currentTime = Clock.systemUTC().millis();
            Pair<String, List<Record>> iteratorRecordsPair =
                kinesisConsumerService.pollForRecords("my-stream", shardIterator);
            shardIterator = iteratorRecordsPair.left();

            // Take these records, map them to events,
            // and record the times using the state service
            List<Record> records = iteratorRecordsPair.right();

            readEvent(records, currentTime);
        }
        log.info("stopped");
    }

    public void readEvent(List<Record> records, long currentTime) {
        records.forEach(record -> {
            byte[] eventBytes = record.data().asByteArray();
            EventProtos.Event event = null;

            try {
                event = EventProtos.Event.parseFrom(eventBytes);
            } catch (Exception e) {
                log.error("Error has occurred!");
            }

            if (log.isInfoEnabled()) {
                log.info("Read event id with " + event.getId());
            }
            timeService.timeMeasurement(event, currentTime);
        });
    }
}
