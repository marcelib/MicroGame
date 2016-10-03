package com.marcelib.microplayer.model;

/**
 * Created by Marceli on 03.10.2016.
 * The code is under the MIT license.
 */
public class Brick {
    private int x1, y1, x2, y2;

    public Brick(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public int getX1() {
        return x1;
    }
}