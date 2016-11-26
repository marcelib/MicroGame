package com.marcelib.discoveryclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServiceRegistryApplication {

    public static void main (String[] args) {
        SpringApplication.run(EurekaServiceRegistryApplication.class, args);
    }
}