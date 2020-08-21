package com.jin.woof.service;

import com.jin.woof.config.WoofConfig;
import com.jin.woof.models.EventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jin.event.proto.EventProtos;
import java.time.Clock;
import java.util.Arrays;
import java.util.UUID;

@Service
public class GoalieService {

    private static final MediaType APPLICATION_PROTOBUF = MediaType.valueOf("application/x-protobuf");
    private static final MediaType APPLICATION_JSON = MediaType.valueOf("application/json");
    private final WoofConfig woofConfig;
    private final RestTemplate restTemplate;
    private final String url;
    private final StateService stateService;

    @Autowired
    public GoalieService(WoofConfig woofConfig, RestTemplate restTemplate, StateService stateService) {
        this.woofConfig = woofConfig;
        this.restTemplate = restTemplate;
        this.url = String.format("%s/events", woofConfig.baseUrl);
        this.stateService = stateService;
    }

    @Scheduled(fixedRate = 5000)
    public EventProtos.Event sendDeviceEvent() throws Exception {
        long startTime = Clock.systemUTC().millis();
        EventProtos.Event event = buildDeviceEvent(woofConfig.getDeviceId());
        HttpEntity<byte[]> httpEntity = buildGoalieHttpEntity(event);
        boolean sendEventSuccess = sendEventToGoalie(httpEntity);

        if (!sendEventSuccess) {
            throw new Exception("something went wrong");
        }
        stateService.set(event.getId(), startTime);

        return event;
    }

    private EventProtos.Event buildDeviceEvent(String deviceId) {
        EventProtos.Event goalieEvent = EventProtos.Event.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setProviderTime(Clock.systemUTC().millis())
                .setDeviceEvent(EventProtos.DeviceEvent.newBuilder().setDeviceId(deviceId).build())
                .build();

        return goalieEvent;
    }

    private HttpEntity<byte[]> buildGoalieHttpEntity(EventProtos.Event event) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_PROTOBUF);
        headers.setAccept(Arrays.asList(APPLICATION_JSON));
        HttpEntity<byte[]> entity = new HttpEntity<>(event.toByteArray(), headers);

        return entity;
    }

    private boolean sendEventToGoalie(HttpEntity<byte[]> entity) {
        ResponseEntity<EventResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                EventResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            return false;
        }
    }
}



