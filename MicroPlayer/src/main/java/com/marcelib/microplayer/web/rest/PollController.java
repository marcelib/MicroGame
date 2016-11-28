package com.marcelib.microplayer.web.rest;

import com.marcelib.microplayer.beans.ConnectionBean;
import com.marcelib.microplayer.web.response.PollResponse;
import com.marcelib.microplayer.web.server.ConnectedServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
@RestController
public class PollController {

    private static final String template = "Status: OK";
    private static final String TYPE = "Player";
    private static final Logger LOGGER = Logger.getLogger(PollController.class.getName());
    private final long playerKey = new Random().nextLong();

    private final ConnectionBean connectionBean;

    private final DiscoveryClient discoveryClient;

    @Autowired
    public PollController (ConnectionBean connectionBean, DiscoveryClient discoveryClient) {
        this.connectionBean = connectionBean;
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping("/poll")
    public PollResponse respond () {
        LOGGER.info("Poll request received, returning id " + playerKey);
        return new PollResponse(playerKey, template);
    }

    @RequestMapping("/recognize")
    public PollResponse getType () {
        LOGGER.info("Recognize request received, returning id " + playerKey + " and type " + TYPE);
        return new PollResponse(playerKey, TYPE);
    }

    public boolean judgeRecognizeRequests () {
        List<ServiceInstance> judgeInstanceList = discoveryClient.getInstances("micro-judge");
        judgeInstanceList.forEach(serviceInstance -> sendJudgeRecognizes(serviceInstance.getHost(), serviceInstance.getPort()));
        return connectionBean.getConnectedServer() != null;
    }

    public void judgePollRequests () {
        List<ServiceInstance> judgeInstanceList = discoveryClient.getInstances("micro-judge");
        judgeInstanceList.forEach(serviceInstance -> sendJudgePolls(serviceInstance.getHost(), serviceInstance.getPort()));
    }

    private boolean sendJudgeRecognizes (String host, int port) {
        String url = "http://" + host + ":" + port + "/recognize";
        RestTemplate restTemplate = new RestTemplate();
        PollResponse response = restTemplate.getForObject(url, PollResponse.class);
        if (response.getResponseKey().equals("Judge")) {
            ConnectedServer connectedServer = new ConnectedServer(response.getKey());
            if ((connectionBean.getConnectedServer() == null)) {
                connectionBean.setConnectedServer(connectedServer);
                LOGGER.info("Judge recognized");
                return true;
            }
        }
        return false;
    }

    private void sendJudgePolls (String host, int port) {
        PollResponse response = new RestTemplate().getForObject("http://" + host + ":" + port + "/poll", PollResponse.class);
        if (response.getKey() != connectionBean.getConnectedServer().getId()) {
            LOGGER.info("Invalid game keys! Duplicate judge instances are not recommmended.");
            System.exit(1);
        } else {
            LOGGER.info("Poll successfully returned from judge");
        }
    }
}
