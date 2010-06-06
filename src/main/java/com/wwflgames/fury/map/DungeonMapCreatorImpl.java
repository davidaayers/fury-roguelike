package com.wwflgames.fury.map;

import com.wwflgames.fury.map.generation.*;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.monster.MonsterFactory;
import com.wwflgames.fury.monster.ai.AI;
import com.wwflgames.fury.monster.ai.RandomMonsterAI;
import com.wwflgames.fury.util.AsciiMapPrinter;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Rand;
import com.wwflgames.fury.util.Shuffler;

import java.util.ArrayList;
import java.util.List;

public class DungeonMapCreatorImpl implements DungeonMapCreator {

    private MonsterFactory monsterFactory;

    public DungeonMapCreatorImpl(MonsterFactory monsterFactory) {
        this.monsterFactory = monsterFactory;
    }

    @Override
    public DungeonMap createMap(DifficultyLevel difficulty, int level) {

        // create a map of random size, based on the difficulty
        int min = difficulty.getLevelSizeMin();
        int max = difficulty.getLevelSizeMax();
        int height = Rand.between(min, max);
        int width = Rand.between(min, max);

        DungeonMap map = new DungeonMap(width, height);

        List<Digger> diggers = new ArrayList<Digger>();
        diggers.add(new RoomDigger(5, 5, 8, 8));
        diggers.add(new RoomDigger(6, 6, 10, 10));
        diggers.add(new SquareRoomDigger(4, 7));

        Log.debug("width = " + width);
        Log.debug("height = " + height);

        // pick a location in the middle-ish of the map,
        int middleX = (width / 2) + Rand.between(-5, 5);
        int middleY = (height / 2) + Rand.between(-5, 5);

        Log.debug("middleX = " + middleX);
        Log.debug("middleY = " + middleY);

        Direction startingDir = Direction.CARDINALS[Rand.get().nextInt(4)];
        JoinPoint starterPoint = new JoinPoint(middleX, middleY, startingDir);

        int numFeatures = 0;
        int tryCount = 0;
        List<JoinPoint> unconnectedPoints = new ArrayList<JoinPoint>();
        List<JoinPoint> connectedPoints = new ArrayList<JoinPoint>();
        unconnectedPoints.add(starterPoint);
        while (numFeatures < 10 && tryCount < 100) {
            Shuffler.shuffle(diggers);
            Digger d = diggers.get(0);
            Log.debug("Using digger " + d);
            try {
                // grab a random unconnected point
                Shuffler.shuffle(unconnectedPoints);
                JoinPoint p = unconnectedPoints.get(0);
                Log.debug("Trying join point " + p);
                Feature f = d.dig(map, p);
                if (f != null) {
                    map.addFeature(f);
                    Log.debug("Sucessfully dug a feature!");
                    AsciiMapPrinter.printMap(map);
                    connectedPoints.add(p);
                    unconnectedPoints.remove(p);
                    p.setConnected(true);
                    for (JoinPoint point : f.getJoinPoints()) {
                        if (!point.isConnected()) {
                            unconnectedPoints.add(point);
                        }
                    }
                    numFeatures++;

                    // populate the newly created feature with monsters
                    populateFeature(map, f, difficulty, level);

                }
            } catch (DigException e) {
                Log.debug("Couldn't dig");
            }
            tryCount++;
        }

        for (JoinPoint jp : unconnectedPoints) {
            map.getTileAt(jp.getX(), jp.getY()).setType(TileType.WALL);
        }

        for (JoinPoint jp : connectedPoints) {
            map.getTileAt(jp.getX(), jp.getY()).setType(TileType.FLOOR);
        }

//        // go through the map and replace all the join points
//        //TODO: make these be doors and such
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                Tile existing = map.getTileAt(x,y);
//                if ( existing.getType() == TileType.JOIN ) {
//                    existing.setType(TileType.FLOOR);
//                }
//            }
//        }

        AsciiMapPrinter.printMap(map);

        return map;
    }

    private void populateFeature(DungeonMap map, Feature feature, DifficultyLevel difficulty, int level) {
        //TODO: stuff based on difficulty.
        Tile[] floorTiles = feature.getFloorTiles();
        int numMonsters = floorTiles.length / 6;
        int cnt = 0;
        while (cnt < numMonsters) {
            Tile randomTile = floorTiles[Rand.get().nextInt(floorTiles.length)];
            if (randomTile.getMob() == null) {
                Monster monster = monsterFactory.createMonster(level);
                Log.debug("Adding monsters " + monster.name());
                map.addMob(monster, randomTile.getX(), randomTile.getY());

                //TODO: move this somewhere that makes sense, certainly not here
                // create the ai for the monster
                AI monsterAi = new RandomMonsterAI(monster,map);
                monster.setAi(monsterAi);

                cnt++;
            }
        }
    }

}
