package com.jin.woof.service

import jin.event.proto.EventProtos
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kinesis.model.Record
import spock.lang.Specification

import java.time.Clock

class SyntheticMonitorServiceSpec extends Specification {
    KinesisConsumerService kinesisConsumerService
    TimeService timeService
    SyntheticMonitorService syntheticMonitorService

    void setup() {
        kinesisConsumerService = Mock()
        timeService = Mock()
        syntheticMonitorService = new SyntheticMonitorService(kinesisConsumerService, timeService)
        0 * _
    }

    void 'readEvent'() {
        given:
        EventProtos.Event event = EventProtos.Event.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setProviderTime(Clock.systemUTC().millis())
            .setDeviceEvent(EventProtos.DeviceEvent.newBuilder().setDeviceId("deviceId").build())
            .build()
        byte[] eventBytes = event.toByteArray()
        Record recordToRead = Record.builder()
            .data(SdkBytes.fromByteArray(eventBytes))
            .build()
        List<Record> records = [recordToRead, recordToRead]
        long currentTime = Clock.systemUTC().millis()

        when:
        syntheticMonitorService.readEvent(records, currentTime)

        then:
        2 * timeService.timeMeasurement(event, currentTime)
    }
}
