package com.marcelib.microjudge.web.rest;

import com.marcelib.microjudge.web.response.PollResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
@RestController
public class PollController {

    private static final String TEMPLATE = "Status: OK";
    private static final String TYPE = "Judge";
    //final after starting the server- unable to change
    private final long gameKey = new Random().nextLong();

    @RequestMapping("/poll")
    public PollResponse respond() {
        return new PollResponse(gameKey, TEMPLATE);
    }

    @RequestMapping("/recognize")
    public PollResponse getType() {
        return new PollResponse(gameKey, TYPE);
    }
}
