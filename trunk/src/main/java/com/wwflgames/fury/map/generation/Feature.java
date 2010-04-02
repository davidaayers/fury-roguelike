package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.map.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * a feature is something that can exist in the dungeon, like rooms
 * and corridors
 */
public class Feature {
    // all of the join points that exist in this feature
    private List<JoinPoint> joinPoints = new ArrayList<JoinPoint>();
    // all of the floor tiles that exist in this feature
    private List<Tile> floorTiles = new ArrayList<Tile>();
    // all of the wall tiles that exist in this feature
    private List<Tile> wallTiles = new ArrayList<Tile>();
    // all tiles
    private List<Tile> allTiles = new ArrayList<Tile>();

    public void addJoinPoint(JoinPoint jp) {
        joinPoints.add(jp);
    }

    public JoinPoint[] getJoinPoints() {
        return joinPoints.toArray(new JoinPoint[0]);
    }

    public void addFloorTile(Tile tile) {
        floorTiles.add(tile);
        allTiles.add(tile);
    }

    public Tile[] getFloorTiles() {
        return floorTiles.toArray(new Tile[0]);
    }

    public void addWallTile(Tile tile) {
        wallTiles.add(tile);
        allTiles.add(tile);
    }

    public Tile[] getWallTiles() {
        return wallTiles.toArray(new Tile[0]);
    }

    public List<Tile> getAllTiles() {
        return allTiles;
    }
}
