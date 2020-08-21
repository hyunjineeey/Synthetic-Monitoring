package com.jin.woof.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jin.event.proto.EventProtos;

import java.util.*;

@Service
@Slf4j
public class TimeService {

    private final StateService stateService;
    List<Long> allOfElapsedTime = new ArrayList<>();

    @Autowired
    public TimeService(StateService stateService) {
        this.stateService = stateService;
    }

    public Long updateAverageTime(List<Long> allOfElapsedTime) {
        int countNumOfEvent = allOfElapsedTime.size();
        long sumOfElapsedTime = 0;
        long avg;

        for (Long time : allOfElapsedTime) {
            sumOfElapsedTime += time;
        }

        avg = sumOfElapsedTime / countNumOfEvent;
        if (log.isInfoEnabled()) {
            log.info("Average time: " + avg);
        }

        return avg;
    }

    public Long updateMaxTime(List<Long> allOfElapsedTime) {
        long maxTime = Collections.max(allOfElapsedTime);

        if (log.isInfoEnabled()) {
            log.info("Max Time: " + maxTime);
        }

        return maxTime;
    }

    public Long updateMinTime(List<Long> allOfElapsedTime) {
        long minTime = Collections.min(allOfElapsedTime);

        if (log.isInfoEnabled()) {
            log.info("Min Time: " + minTime);
        }

        return minTime;
    }

    public void timeMeasurement(EventProtos.Event event, long currentTime) {
        long elapsedTime;
        Long eventTime = stateService.get(event.getId());

        if (eventTime != null) {
            elapsedTime = currentTime - eventTime;
            allOfElapsedTime.add(elapsedTime);

            if (log.isInfoEnabled()) {
                log.info("Time for " + event.getId() + ": " + elapsedTime);
            }

            updateAverageTime(allOfElapsedTime);
            updateMaxTime(allOfElapsedTime);
            updateMinTime(allOfElapsedTime);
        }
    }
}
