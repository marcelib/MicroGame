package com.marcelib.microjudge.web.client;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
public class ConnectedEntity {
    private final long id;
    private boolean starting;

    public ConnectedEntity(long id, boolean starting) {
        this.id = id;
        this.starting = starting;
    }

    public long getId() {
        return id;
    }

    public boolean isStarting() {
        return starting;
    }
}
