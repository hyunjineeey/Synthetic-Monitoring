package com.jin.woof.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StateService {
    private final Map<String, Long> map = new ConcurrentHashMap<>();

    @Autowired
    public StateService() {
    }

    public Long get(String eventId) {
        return map.get(eventId);
    }

    public void set(String eventId, long time) {
        map.put(eventId, time);
    }
}
