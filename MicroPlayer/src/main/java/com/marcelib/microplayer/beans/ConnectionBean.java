package com.marcelib.microplayer.beans;

import com.marcelib.microplayer.web.server.ConnectedServer;
import org.springframework.stereotype.Component;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
@Component
public class ConnectionBean {

    private ConnectedServer connectedServer;

    public ConnectionBean() {
        this.connectedServer = null;
    }

    public ConnectedServer getConnectedServer() {
        return connectedServer;
    }

    public void setConnectedServer(ConnectedServer connectedServer) {
        this.connectedServer = connectedServer;
    }
}
