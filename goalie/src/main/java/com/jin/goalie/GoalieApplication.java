package com.jin.goalie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class GoalieApplication {
    public static void main(String[] args){
        System.setProperty("org.apache.flink.kinesis.shaded.com.amazonaws.sdk.disableCbor", "true");
        System.setProperty("aws.cborEnabled", "false");
        SpringApplication.run(GoalieApplication.class, args);
    }
}
