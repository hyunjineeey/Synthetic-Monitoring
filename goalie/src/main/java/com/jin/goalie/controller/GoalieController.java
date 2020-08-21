package com.jin.goalie.controller;

import com.jin.goalie.models.EventResponse;
import com.jin.goalie.service.EventService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jin.event.proto.EventProtos;

@Slf4j
@RestController()
@RequestMapping(path = "/events")
public class GoalieController {

    private final EventService eventService;

    @Autowired
    public GoalieController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(consumes = "application/x-protobuf")
    @SneakyThrows
    public EventResponse postEvent(@RequestBody byte[] eventBytes) {
        EventProtos.Event event = EventProtos.Event.parseFrom(eventBytes);
        if (log.isInfoEnabled()) {
            log.info("Received event with id: " + event.getId());
        }
        eventService.sendSomeMessage(eventBytes);

        return new EventResponse(event.getId());
    }

}

