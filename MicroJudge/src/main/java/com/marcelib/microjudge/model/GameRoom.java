package com.marcelib.microjudge.model;

import com.marcelib.microjudge.web.client.ConnectedEntity;

public class GameRoom {

    private ConnectedEntity player1;
    private ConnectedEntity player2;

    public ConnectedEntity getPlayer1() {
        return player1;
    }

    public ConnectedEntity getPlayer2() {
        return player2;
    }


    public boolean isFull() {
        return player1 != null && player2 != null;
    }

    public void addPlayer(ConnectedEntity connectedEntity) {
        if (player1 != null) {
            if (player2 != null) {
                player2 = connectedEntity;
            }
        } else {
            player1 = connectedEntity;
        }
    }

    public void startMatch(){

    }
}
