package com.jin.woof.service

import com.jin.woof.config.WoofConfig
import com.jin.woof.models.EventResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import jin.event.proto.EventProtos
import spock.lang.Specification
import java.time.Clock

class GoalieServiceSpec extends Specification {

    String baseUrl = "http://example.com"
    String deviceId = UUID.randomUUID().toString()
    String locationId = UUID.randomUUID().toString()

    WoofConfig config
    RestTemplate restTemplate
    GoalieService goalieService
    StateService stateService

    void setup() {
        config = new WoofConfig(baseUrl: baseUrl, deviceId: deviceId, locationId: locationId)
        restTemplate = Mock(RestTemplate)
        goalieService = new GoalieService(config, restTemplate, stateService)
    }

    void 'It will build device event'() {
        given:
        String deviceId = "abc123"

        when:
        EventProtos.Event event = goalieService.buildDeviceEvent(deviceId)

        then:
        assert event.deviceEvent.deviceId == deviceId
        event != null
        event.locationId == ""
    }

    void 'It will build http entity'() {
        given:
        EventProtos.Event event = EventProtos.Event.newBuilder()
                .setProviderTime(Clock.systemUTC().millis())
                .setDeviceEvent(EventProtos.DeviceEvent.newBuilder().setDeviceId(deviceId).build())
                .build()

        when:
        HttpEntity<byte[]> entity = goalieService.buildGoalieHttpEntity(event)

        then:
        entity.body == event.toByteArray()
        entity.headers.get("content-type") == ["application/x-protobuf"]
        entity.headers.get("accept") == ["application/json"]
        entity.headers.size() == 2
    }

    void 'The call to my url and response are working'() {
        given:
        HttpHeaders headers = new HttpHeaders()
        HttpEntity<byte[]> entity = new HttpEntity<>([] as byte[], headers)

        ResponseEntity<EventResponse> returnedEntity = new ResponseEntity<EventResponse>(HttpStatus.OK)

        when:
        boolean result = goalieService.sendEventToGoalie(entity)

        then:
        1 * restTemplate.exchange(
                "${baseUrl}/events",
                HttpMethod.POST,
                _,
                EventResponse.class
        ) >> returnedEntity

        and:
        result == true
    }

}
