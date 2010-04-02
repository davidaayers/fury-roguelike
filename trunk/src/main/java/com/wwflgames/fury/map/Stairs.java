package com.wwflgames.fury.map;

public class Stairs {
    private DungeonMap mapA;
    private DungeonMap mapB;
    private Tile mapATile;
    private Tile mapBTile;
    private boolean areLocked;

    public Stairs(DungeonMap mapA, DungeonMap mapB, Tile mapATile, Tile mapBTile) {
        this.mapA = mapA;
        this.mapB = mapB;
        this.mapATile = mapATile;
        this.mapBTile = mapBTile;
    }

    public Tile tileAtOtherEndFrom(DungeonMap map) {
        if (map.equals(mapA)) {
            return mapBTile;
        } else {
            return mapATile;
        }
    }

    public DungeonMap mapAtOtherEndFrom(DungeonMap map) {
        if (map.equals(mapA)) {
            return mapB;
        } else {
            return mapA;
        }
    }

    public boolean areLocked() {
        return areLocked;
    }

    public void setAreLocked(boolean areLocked) {
        this.areLocked = areLocked;
    }
}
