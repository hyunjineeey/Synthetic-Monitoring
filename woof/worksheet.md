# Worksheet / Q&A

## Wednesday July 15

I took the "time" related methods and moved them all to `TimeService`. This should help you focus
on *just* getting those methods to work. I added three tests in `TimeServiceSpec` for the new methods. Your goal should
be to get those to all pass.

`com.jin.woof.service.SyntheticMonitorService`:
- Moved "time" methods to a new service, `TimeService`
- Comments or changes on the following lines:
- `SyntheticMonitorService`:
    - 87
    - 110-111
- `TimeService`
    - 43-46
    - 93-96

TODO:
- Add to `allOfElapsedTime` list each time you consume an event that you "know about" (from stateService!)
- Get TimeServiceSpec tests all passing
- Log out min, max, and average every time you call `timeService.timeMeasurement(event, allOfElapsedTime)` from `SyntheticMonitorService`

## Monday July 20

- I took a measurement of "now" (currentTime) in SyntheticMonitorService. This should be used instead of measuring it
in `TimeService`, as measuring it there will take into account the amount of time it takes to poll kinesis, and we 
just want to measure the time it takes for boucner to receive the messsage, "process it", and put it onto kinesis.

TODO:
- Refactor `SyntheticMonitorService` to use that time instead of measuring it in `timeService.timeMeasurement`

