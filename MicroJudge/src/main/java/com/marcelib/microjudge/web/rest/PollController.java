package com.marcelib.microjudge.web.rest;

import com.marcelib.microjudge.beans.ConnectionBean;
import com.marcelib.microjudge.web.client.ConnectedEntity;
import com.marcelib.microjudge.web.response.PollResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
@RestController
public class PollController {

    private static final String TEMPLATE = "Status: OK";
    private static final String TYPE = "Judge";
    private final static Logger LOGGER = Logger.getLogger(PollController.class.getName());
    private final static int PLAYER_PORT_1 = 9001;
    private final static int PLAYER_PORT_2 = 9002;

    //final after starting the server- unable to change
    private final long gameKey = new Random().nextLong();

    private ConnectionBean connectionBean;

    @Autowired
    public PollController(ConnectionBean connectionBean) {
        this.connectionBean = connectionBean;
    }

    @RequestMapping("/poll")
    public PollResponse respond() {
        LOGGER.info("Poll received, returning id " + gameKey);
        return new PollResponse(gameKey, TEMPLATE);
    }

    @RequestMapping("/recognize")
    public PollResponse getType() {
        LOGGER.info("Request recognize received, returning id " + gameKey);
        return new PollResponse(gameKey, TYPE);
    }

    public boolean sendPlayerPoll(int port) {
        String url = "http://localhost:" + port + "/poll";
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("Sending poll to player");
        PollResponse response = restTemplate.getForObject(url, PollResponse.class);
        return verifyGameKey(response.getKey());
    }

    private boolean verifyGameKey(long gameKey) {
        return connectionBean.getConnectedPlayers().stream().anyMatch(e -> e.getId() == gameKey);
    }

    public boolean sendPlayerRecognize(int port) {
        String url = "http://localhost:" + port + "/recognize";
        RestTemplate restTemplate = new RestTemplate();
        PollResponse response = restTemplate.getForObject(url, PollResponse.class);
        if (response.getResponseKey().equals("Judge")) {
            boolean firstConnection = connectionBean.getConnectedPlayers().size() == 0;
            ConnectedEntity connectedServer = new ConnectedEntity(response.getKey(), firstConnection);
            connectionBean.addConnectedPlayer(connectedServer);
            return true;
        }
        return false;
    }
}
