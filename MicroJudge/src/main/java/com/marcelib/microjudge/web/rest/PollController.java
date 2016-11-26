package com.marcelib.microjudge.web.rest;

import com.marcelib.microjudge.beans.ConnectionBean;
import com.marcelib.microjudge.web.client.ConnectedEntity;
import com.marcelib.microjudge.web.response.PollResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
@RestController
public class PollController {

    private static final String TEMPLATE = "Status: OK";
    private static final String TYPE = "Judge";
    private final static Logger LOGGER = Logger.getLogger(PollController.class.getName());
    private static final String RECOGNIZE_REQUEST = "/recognize";
    private static final String POLL_REQUEST = "/poll";


    //final after starting the server- unable to change
    private final long gameKey = new Random().nextLong();

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
        return new PollResponse(gameKey, TEMPLATE);
    }

    @RequestMapping("/recognize")
    public PollResponse getType () {
        LOGGER.info("Request recognize received, returning id " + gameKey);
        int sizeBefore = connectionBean.getConnectedPlayers().size();
        while (connectionBean.getConnectedPlayers().size() != sizeBefore) {
            try {
                Thread.sleep(2000);
            } catch(InterruptedException e) {
                LOGGER.log(Level.INFO, e.toString());
            }
            sendPlayerRecognize();
        }

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run () {
                sendPlayerPoll();
            }
        }, 0, 2000);
        return new PollResponse(gameKey, TYPE);
    }

    private boolean verifyGameKey (long gameKey) {
        return connectionBean.getConnectedPlayers().stream().anyMatch(e -> e.getId() == gameKey);
    }

    private void sendPlayerRequests (String host, int port, String request) {
        String url = "http://" + host + ":" + port + request;
        RestTemplate restTemplate = new RestTemplate();
        PollResponse response = restTemplate.getForObject(url, PollResponse.class);
        if (response.getResponseKey().equals("Judge") && RECOGNIZE_REQUEST.equals(request)) {
            ConnectedEntity connectedEntity = new ConnectedEntity(response.getKey());
            if (verifyGameKey(gameKey)) {
                connectionBean.addConnectedPlayer(connectedEntity);
            } else {
                List<ConnectedEntity> connectedEntities = connectionBean.getConnectedPlayers()
                        .stream().filter(e -> e.getId() == response.getKey()).collect(Collectors.toList());
                if (connectedEntities.size() == 1) {
                    connectionBean.addConnectedPlayer(connectedEntity);
                }

            }
        }
    }


    public boolean sendPlayerRecognize () {
        List<ServiceInstance> judgeInstanceList = discoveryClient.getInstances("micro-player");
        judgeInstanceList.forEach(serviceInstance -> sendPlayerRequests(serviceInstance.getHost(), serviceInstance.getPort(), RECOGNIZE_REQUEST));
        return !connectionBean.getConnectedPlayers().isEmpty();
    }

    public boolean sendPlayerPoll () {
        List<ServiceInstance> judgeInstanceList = discoveryClient.getInstances("micro-player");
        judgeInstanceList.forEach(serviceInstance -> sendPlayerRequests(serviceInstance.getHost(), serviceInstance.getPort(), POLL_REQUEST));
        return !connectionBean.getConnectedPlayers().isEmpty();
    }

}
