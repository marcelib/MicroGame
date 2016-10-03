package com.marcelib.microplayer.model;

import java.util.List;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
public class Board {

    private List<List<Cell>> cells;

    public Board(List<List<Cell>> cells) {
        this.cells = cells;
    }

    public List<List<Cell>> getCells() {
        return cells;
    }

    public Cell getCell(int x, int y) {
        return cells.get(x).get(y);
    }
}
