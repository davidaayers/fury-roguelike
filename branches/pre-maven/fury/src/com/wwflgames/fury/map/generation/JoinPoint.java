package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.map.Direction;

public class JoinPoint {
    private int x;
    private int y;
    private Direction direction;
    boolean connected = false;

    public JoinPoint(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public String toString() {
        return "JoinPoint{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                ", connected=" + connected +
                '}';
    }
}
