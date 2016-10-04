package com.marcelib.microplayer.web.rest;

import com.marcelib.microplayer.beans.ConnectionBean;
import com.marcelib.microplayer.web.response.PollResponse;
import com.marcelib.microplayer.web.server.ConnectedServer;
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

    private static final String template = "Status: OK";
    private static final String TYPE = "Player";
    private static final int JUDGE_PORT = 9000;
    private static final Logger LOGGER = Logger.getLogger(PollController.class.getName());
    //final after starting the server- unable to change
    private final long gameKey = new Random().nextLong();

    private final ConnectionBean connectionBean;

    @Autowired
    public PollController(ConnectionBean connectionBean) {
        this.connectionBean = connectionBean;
    }

    @RequestMapping("/poll")
    public PollResponse respond() {
        return new PollResponse(gameKey, template);
    }

    @RequestMapping("/recognize")
    public PollResponse getType() {
        return new PollResponse(gameKey, TYPE);
    }

    public void sendJudgePoll() {
        String url = "http://localhost:" + JUDGE_PORT + "/poll";
        RestTemplate restTemplate = new RestTemplate();
        LOGGER.info("Sending poll to judge");
        PollResponse response = restTemplate.getForObject(url, PollResponse.class);
        if (response.getKey() != connectionBean.getConnectedServer().getId()) {
            LOGGER.info("Invalid game keys! Program will now exit.");
            System.exit(1);
        } else {
            LOGGER.info("Poll successfully returned from judge");
        }
    }

    public boolean sendJudgeRecognize() {
        String url = "http://localhost:" + JUDGE_PORT + "/recognize";
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
}
