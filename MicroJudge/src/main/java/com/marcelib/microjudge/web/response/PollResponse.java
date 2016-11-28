package com.marcelib.microjudge.web.response;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
public class PollResponse {

    private long key;
    private String responseKey;

    public PollResponse () {
    }

    public PollResponse (long key, String responseKey) {
        this.key = key;
        this.responseKey = responseKey;
    }

    public String getResponseKey () {
        return responseKey;
    }

    public long getKey () {
        return key;
    }
}
