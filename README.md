# A synthetic monitoring system.

## Table of contents

-   [General Info](#General-Info)
-   [Prerequisites](#Prerequisites)
-   [Setup](#Setup)
-   [How to Run](#How-to-Run)
-   [Technologies](#Technologies)

## General Info

The synthetic monitoring system measures the time of the process starting with an API call and ending with event delivery. It returns the total time is taken, minimum, maximum, and average time.

## Prerequisites

-   Must use `Open JDK 11`

## Setup

To run this project, run docker locally:

```
$ docker-compose up -d
```

## Protocol Buffers

This project uses Google's protocol buffers for the event definition.  
To generate the protocol buffer Java classes run:

```
$ ./gradlew goalie-api:build
```

## How to Run

-   Open two terminal for `goalie` and `woof`

```
$ ./gradlew goalie:bootRun
```

It will be ready to receive events from `woof`

```
$ ./gradlew woof:bootRun
```

It will send events to `goalie` in every 5 seconds and read the events.

## Technologies

-   Java
-   Spring Boot
-   Gradle
-   Spock
-   Groovy
