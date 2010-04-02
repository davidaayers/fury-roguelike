package com.wwflgames.fury.map;

import com.wwflgames.fury.map.generation.Feature;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.monster.MonsterDeathActivity;
import com.wwflgames.fury.monster.MonsterFactory;
import com.wwflgames.fury.util.Rand;

import java.util.ArrayList;
import java.util.List;

public class DungeonCreatorImpl implements DungeonCreator {

    private DungeonMapCreator mapCreator;
    private MonsterFactory monsterFactory;

    public DungeonCreatorImpl(DungeonMapCreator mapCreator, MonsterFactory monsterFactory) {
        this.mapCreator = mapCreator;
        this.monsterFactory = monsterFactory;
    }

    @Override
    public Dungeon createDungeon(DifficultyLevel difficulty) {

        int floorsToCreate = difficulty.getNumLevelsDeep();

        List<DungeonMap> levels = new ArrayList<DungeonMap>();

        for (int idx = 0; idx < floorsToCreate; idx++) {
            levels.add(mapCreator.createMap(difficulty, idx + 1));
        }

        // now that we've created all the levels, go through and add stairs
        // from one level to the next
        for (int idx = 0; idx < floorsToCreate - 1; idx++) {
            DungeonMap mapA = levels.get(idx);
            DungeonMap mapB = levels.get(idx + 1);
            List<Feature> features = mapA.getFeatureList();
            Feature f = features.get(Rand.get().nextInt(features.size()));
            Tile mapATile = chooseRandomTileInFeature(f);
            mapATile.setType(TileType.STAIR);
            Tile mapBTile = findRandomTile(mapB);
            mapBTile.setType(TileType.STAIR);
            Stairs s = new Stairs(mapA, mapB, mapATile, mapBTile);
            s.setAreLocked(true);
            mapATile.setStairs(s);
            mapBTile.setStairs(s);
            placeBossMonsterInFeature(mapA, f, idx, s);
        }

        // finally, place the Big Boss of the dungeon somewhere on the last level
        DungeonMap lastLevel = levels.get(levels.size()-1); 

        Dungeon dungeon = new Dungeon(levels);

        return dungeon;
    }

    private void placeBossMonsterInFeature(DungeonMap map, Feature f, int level, final Stairs s) {
        Tile bossTile = null;
        while (bossTile == null) {
            Tile checkTile = chooseRandomTileInFeature(f);
            if (checkTile.getMob() == null && checkTile.getType() != TileType.STAIR) {
                bossTile = checkTile;
            }
        }
        Monster boss = monsterFactory.createBossMonster(level + 1);
        boss.addMonsterDeathActivity(new MonsterDeathActivity() {
            @Override
            public void doActivity() {
                s.setAreLocked(false);
            }
        });
        map.addMob(boss, bossTile.getX(), bossTile.getY());
    }

    private Tile findRandomTile(DungeonMap map) {
        List<Feature> features = map.getFeatureList();
        Feature f = features.get(Rand.get().nextInt(features.size()));
        return chooseRandomTileInFeature(f);
    }

    private Tile chooseRandomTileInFeature(Feature f) {
        Tile[] floorTiles = f.getFloorTiles();
        Tile chosenTile = null;
        while ( chosenTile == null ) {
            Tile randomTile = floorTiles[Rand.get().nextInt(floorTiles.length)];
            if ( randomTile.getMob() == null ) {
                chosenTile = randomTile;
            }
        }
        return chosenTile;
    }
}
