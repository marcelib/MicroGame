package com.marcelib.microjudge.beans;

import com.marcelib.microjudge.web.client.ConnectedEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
@Component
public class ConnectionBean {

    private List<ConnectedEntity> connectedPlayers;

    public ConnectionBean() {
        this.connectedPlayers = new ArrayList<>();
    }

    public List<ConnectedEntity> getConnectedPlayers() {
        return connectedPlayers;
    }

    public void addConnectedPlayer(ConnectedEntity connectedPlayer) {
        if (connectedPlayers.size() < 2) {
            connectedPlayers.add(connectedPlayer);
        }
    }
}
