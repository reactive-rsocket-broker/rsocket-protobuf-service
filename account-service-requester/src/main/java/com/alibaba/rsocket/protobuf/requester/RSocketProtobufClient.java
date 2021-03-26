package com.alibaba.rsocket.protobuf.requester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RSocketProtobufClient {

    public static void main(String[] args) {
        SpringApplication.run(RSocketProtobufClient.class, args);
    }

}
