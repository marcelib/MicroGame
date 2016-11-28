package com.marcelib.microjudge.web.rest;

import com.marcelib.microjudge.beans.ConnectionBean;
import com.marcelib.microjudge.web.client.ConnectedEntity;
import com.marcelib.microjudge.web.response.PollResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

@RestController
public class PollController {

    private static final String TEMPLATE = "Status: OK";
    private static final String TYPE = "Judge";
    private final static Logger LOGGER = Logger.getLogger(PollController.class.getName());
    private static final String RECOGNIZE_REQUEST = "/recognize";
    private static final String POLL_REQUEST = "/poll";
    private final long gameKey = new Random().nextLong();
    private Timer pollTimer = new Timer();
    private boolean pollTimerStarted = false;
    private DiscoveryClient discoveryClient;
    private ConnectionBean connectionBean;

    @Autowired
    public PollController (DiscoveryClient discoveryClient, ConnectionBean connectionBean) {
        this.discoveryClient = discoveryClient;
        this.connectionBean = connectionBean;
    }

    @RequestMapping("/poll")
    public PollResponse respond () {
        LOGGER.info("Poll received, returning id " + gameKey);
        schedulePlayerPolls();
        return new PollResponse(gameKey, TEMPLATE);
    }

    @RequestMapping("/recognize")
    public PollResponse getType () {
        LOGGER.info("Request recognize received, returning id " + gameKey);
        sendPlayersRecognize();
        return new PollResponse(gameKey, TYPE);
    }

    void sendPlayersRecognize () {
        discoveryClient.getInstances("micro-player")
                .forEach(serviceInstance -> playerRecognizeRequest(serviceInstance.getHost(), serviceInstance.getPort()));
    }

    boolean sendPlayersPoll () {
        discoveryClient.getInstances("micro-player")
                .forEach(serviceInstance -> playerPollRequest(serviceInstance.getHost(), serviceInstance.getPort()));
        return !connectionBean.getConnectedPlayers().isEmpty();
    }

    private boolean verifyEntityId (long entityId) {
        return connectionBean.getConnectedPlayers().stream().anyMatch(e -> e.getId() == entityId);
    }

    private void playerPollRequest (String host, int port) {
        PollResponse response = new RestTemplate().getForObject("http://" + host + ":" + port + POLL_REQUEST, PollResponse.class);
        if (!connectionBean.getConnectedPlayers().stream().anyMatch(e -> e.getId() == response.getKey())) {
            LOGGER.info("New player detected while polling! Scheduling recognize request");
            sendPlayersRecognize();
        } else {
            LOGGER.info("Poll successfully returned from player with id " + response.getKey());
        }
    }

    private void playerRecognizeRequest (String host, int port) {
        PollResponse response = new RestTemplate().getForObject("http://" + host + ":" + port + RECOGNIZE_REQUEST, PollResponse.class);
        if (response.getResponseKey().equals("Player")) {
            ConnectedEntity connectedEntity = new ConnectedEntity(response.getKey());
            if (!verifyEntityId(connectedEntity.getId())) {
                connectionBean.addConnectedPlayer(connectedEntity);
            }
        }
        schedulePlayerPolls();
    }

    private void schedulePlayerPolls () {
        if (!pollTimerStarted) {
            pollTimerStarted = true;
            pollTimer.schedule(new TimerTask() {
                public void run () {
                    sendPlayersPoll();
                }
            }, 0, 2000);
        }
    }
}