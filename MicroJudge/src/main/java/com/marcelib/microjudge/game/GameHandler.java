package com.marcelib.microjudge.game;

import com.marcelib.microjudge.model.GameRoom;
import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GameHandler {

    Map<Integer, Pair<GameRoom, Boolean>> gameRooms;

    public void initiateGame() {
    }
}
