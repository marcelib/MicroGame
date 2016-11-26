package com.marcelib.microjudge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.marcelib"})
public class MicroJudgeApplication {

    public static void main (String[] args) {
        SpringApplication.run(MicroJudgeApplication.class, args);
    }
}

@RestController
class ServiceInstanceRestController {

    private final DiscoveryClient discoveryClient;

    @Autowired
    public ServiceInstanceRestController (DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName (
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }
}