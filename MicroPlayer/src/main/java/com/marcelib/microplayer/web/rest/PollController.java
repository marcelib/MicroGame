package com.marcelib.microplayer.web.rest;

import com.marcelib.microplayer.MicroPlayerApplication;
import com.marcelib.microplayer.beans.ConnectionBean;
import com.marcelib.microplayer.web.response.PollResponse;
import com.marcelib.microplayer.web.server.ConnectedServer;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
@RestController
public class PollController {

    private static final String template = "Status: OK";
    private static final String TYPE = "Player";
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

    public boolean sendJudgePoll() {
        String url = "http://localhost:9000/poll";
        RestTemplate restTemplate = new RestTemplate();
        PollResponse response = restTemplate.getForObject(url, PollResponse.class);
        return false;
    }

    public boolean sendJudgeRecognize() {
        String url = "http://localhost:9000/recognize";
        RestTemplate restTemplate = new RestTemplate();
        PollResponse response = restTemplate.getForObject(url, PollResponse.class);
        if (response.getResponseKey().equals("Judge")) {
            ConnectedServer connectedServer = new ConnectedServer(response.getKey());
            if ((connectionBean.getConnectedServer() == null)) {
                connectionBean.setConnectedServer(connectedServer);
                System.out.println("Connection success!");
                return true;
            }
        }
        return false;
    }
}
