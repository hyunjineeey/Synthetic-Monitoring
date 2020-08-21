package com.jin.woof.service

import spock.lang.Specification

class TimeServiceSpec extends Specification {

    StateService stateService
    TimeService timeService

    void setup() {
        stateService = new StateService()
        timeService = new TimeService(stateService)
    }

    void 'The average of 1,2,3 is 2'() {
        given:
        List<Long> myList = [1L, 2L, 3L]

        when:
        Long average = timeService.updateAverageTime(myList)

        then:
        average == 2L
    }

    void 'The max of 1,2,3 is 3'() {
        given:
        List<Long> myList = [1L, 2L, 3L]

        when:
        Long max = timeService.updateMaxTime(myList)

        then:
        max == 3L
    }

    void 'The min of 1,2,3 is 1'() {
        given:
        List<Long> myList = [1L, 2L, 3L]

        when:
        Long min = timeService.updateMinTime(myList)

        then:
        min == 1L
    }
}
