package com.wwflgames.fury.map;

import com.wwflgames.fury.mob.Mob;

public class Tile {

    public static final int NOT_VISIBLE = 0;
    public static final int FULLY_VISIBLE = 5;


    private TileType type;
    private int x;
    private int y;
    // the mob that's occupying this tile
    private Mob mob;
    private boolean hasPlayerSeen;
    private int playerVisibility;
    private Stairs stairs;
    private DungeonMap owningMap;

    public Tile(DungeonMap owningMap, TileType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.owningMap = owningMap;
        hasPlayerSeen = false;
        playerVisibility = 0;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Mob getMob() {
        return mob;
    }

    // package private so it can only be called from DungeonMap
    void setMob(Mob mob) {
        this.mob = mob;
    }

    public boolean isWalkable() {
        return type.isWalkable();
    }

    public boolean hasPlayerSeen() {
        return hasPlayerSeen;
    }

    public void setHasPlayerSeen(boolean hasPlayerSeen) {
        this.hasPlayerSeen = hasPlayerSeen;
    }

    public int getPlayerVisibility() {
        return playerVisibility;
    }

    public void setPlayerVisibility(int playerVisibility) {
        this.playerVisibility = playerVisibility;
    }

    public boolean isVisible() {

        if (type == TileType.WALL) {
            return this.hasPlayerSeen();
        } else {
            return this.playerVisibility != 0;
        }
    }

    public Stairs getStairs() {
        return stairs;
    }

    public void setStairs(Stairs stairs) {
        this.stairs = stairs;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", mob=" + mob +
                '}';
    }
}
