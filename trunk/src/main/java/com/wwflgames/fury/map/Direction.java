package com.wwflgames.fury.map;

public enum Direction {

    N(72, 0, -1, "North"),
    NE(73, 1, -1, "Northeast"),
    E(77, 1, 0, "East"),
    SE(81, 1, 1, "Southeast"),
    S(80, 0, 1, "South"),
    SW(79, -1, 1, "Southwest"),
    W(75, -1, 0, "West"),
    NW(71, -1, -1, "Northwest");

    public static Direction[] CARDINALS = new Direction[]{N, S, E, W};

    private int key;
    private int dx;
    private int dy;
    private String desc;

    Direction(int key, int dx, int dy, String desc) {
        this.key = key;
        this.dx = dx;
        this.dy = dy;
        this.desc = desc;
    }

    public int getKey() {
        return key;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public String getDesc() {
        return desc;
    }

    public static Direction forKey(int key) {
        for (Direction dir : values()) {
            if (dir.key == key) {
                return dir;
            }
        }
        return null;
    }

}
