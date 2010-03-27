package com.wwflgames.fury.map;

import com.wwflgames.fury.map.generation.Feature;
import com.wwflgames.fury.util.Rand;

import java.util.ArrayList;
import java.util.List;

public class DungeonCreatorImpl implements DungeonCreator {

    private DungeonMapCreator mapCreator;

    public DungeonCreatorImpl(DungeonMapCreator mapCreator) {
        this.mapCreator = mapCreator;
    }

    @Override
    public Dungeon createDungeon(DifficultyLevel difficulty) {

        int floorsToCreate = difficulty.getNumLevelsDeep();

        List<DungeonMap> levels = new ArrayList<DungeonMap>();

        for (int idx = 0; idx < floorsToCreate; idx++) {
            levels.add(mapCreator.createMap(difficulty, idx));
        }

        // now that we've created all the levels, go through and add stairs
        // from one level to the next
        for (int idx = 0; idx < floorsToCreate - 1; idx++) {
            DungeonMap mapA = levels.get(idx);
            DungeonMap mapB = levels.get(idx + 1);
            Tile mapATile = findRandomTile(mapA);
            mapATile.setType(TileType.STAIR);
            Tile mapBTile = findRandomTile(mapB);
            mapBTile.setType(TileType.STAIR);
            Stairs s = new Stairs(mapA, mapB, mapATile, mapBTile);
            mapATile.setStairs(s);
            mapBTile.setStairs(s);
        }

        Dungeon dungeon = new Dungeon(levels);

        return dungeon;
    }

    private Tile findRandomTile(DungeonMap mapB) {
        List<Feature> features = mapB.getFeatureList();
        Feature f = features.get(Rand.get().nextInt(features.size()));
        Tile[] floorTiles = f.getFloorTiles();
        return floorTiles[Rand.get().nextInt(floorTiles.length)];
    }
}
