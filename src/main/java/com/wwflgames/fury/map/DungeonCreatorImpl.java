package com.wwflgames.fury.map;

import com.wwflgames.fury.main.AppState;
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
    private AppState appState;

    public DungeonCreatorImpl(DungeonMapCreator mapCreator, MonsterFactory monsterFactory, AppState appState) {
        this.mapCreator = mapCreator;
        this.monsterFactory = monsterFactory;
        this.appState = appState;
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
            Feature f = findRandomFeature(mapA);
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
        Feature f = findRandomFeature(lastLevel);
        Tile bossTile = null;
        while (bossTile == null) {
            Tile t = chooseRandomTileInFeature(f);
            if ( t.getMob() == null && t.getType() != TileType.STAIR ) {
                bossTile = t;       
            }
        }
        Monster bigBoss = monsterFactory.createBossMonster(levels.size()-1);
        bigBoss.addMonsterDeathActivity(new MonsterDeathActivity() {
            @Override
            public void doActivity() {
                appState.setGameOver(true);    
            }
        });
        lastLevel.addMob(bigBoss,bossTile.getX(),bossTile.getY());

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

    private Feature findRandomFeature(DungeonMap map) {
        List<Feature> features = map.getFeatureList();
        return features.get(Rand.get().nextInt(features.size()));
    }

    private Tile findRandomTile(DungeonMap map) {
        Feature f = findRandomFeature(map);
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
