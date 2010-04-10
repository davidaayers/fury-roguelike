package com.wwflgames.fury.map;

import com.wwflgames.fury.map.generation.Feature;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class DungeonMap implements Cloneable {

    private int offsetX;
    private int offsetY;
    private int width;
    private int height;
    private Tile[][] tiles;
    private List<Monster> monsterList = new ArrayList<Monster>();
    private List<Feature> features = new ArrayList<Feature>();

    public DungeonMap(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        initTilesToFloor();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isWalkable(int x, int y) {
        Tile t = getTileAt(x, y);
        return t.isWalkable();
    }

    private void initTilesToFloor() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // default tiles to floor
                tiles[x][y] = new Tile(this, TileType.EMPTY, x, y);
            }
        }
    }

    public Tile getTileAt(int x, int y) {
        return tiles[x][y];
    }

    public void addMob(Mob mob, int x, int y) {
        addMobToTileAt(mob, x, y);
        if (mob instanceof Monster) {
            monsterList.add((Monster) mob);
        }
    }

    public void removeMob(Mob mob) {
        removeMobFromTile(mob);
        if (mob instanceof Monster) {
            monsterList.remove(mob);
        }
    }

    private void addMobToTileAt(Mob mob, int x, int y) {
        Tile tile = getTileAt(x, y);
        tile.setMob(mob);
        mob.setCurrentMapTile(tile);
    }

    private void removeMobFromTile(Mob mob) {
        Tile tile = mob.getCurrentMapTile();
        tile.setMob(null);
        mob.setCurrentMapTile(null);
    }

    public void moveMonster(Monster monster, Tile newTile) {
        removeMobFromTile(monster);
        addMobToTileAt(monster,newTile.getX(),newTile.getY());
    }

    public List<Monster> getMonsterList() {
        return monsterList;
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    public List<Feature> getFeatureList() {
        return features;
    }

    public Feature findFeatureFor(int x, int y) {
        Tile tile = this.getTileAt(x, y);
        for (Feature feature : features) {
            List<Tile> allTiles = feature.getAllTiles();
            if (allTiles.contains(tile)) {
                return feature;
            }
        }
        throw new IllegalStateException("Should not get here");
    }

    public DungeonMap duplicate() {
        DungeonMap copy = new DungeonMap(this.width, this.height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile existing = this.getTileAt(x, y);
                copy.tiles[x][y] = new Tile(copy, existing.getType(), x, y);
            }
        }
        return copy;
    }

    public void resetVisibility() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile existing = this.getTileAt(x, y);
                existing.setPlayerVisibility(Tile.NOT_VISIBLE);
            }
        }
    }
}
