package com.marcelib.microplayer.web.server;


/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
public class ConnectedServer {

    private final long id;

    public ConnectedServer(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
