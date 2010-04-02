package com.wwflgames.fury.map;

public class FixedDungeonMapCreator implements DungeonMapCreator {
    @Override
    public DungeonMap createMap(DifficultyLevel difficulty, int level) {
        DungeonMap dungeonMap = new DungeonMap(5, 5);

        // add walls to the all around
        for (int cnt = 0; cnt < 5; cnt++) {
            dungeonMap.getTileAt(cnt, 0).setType(TileType.WALL);
            dungeonMap.getTileAt(cnt, 4).setType(TileType.WALL);
            dungeonMap.getTileAt(0, cnt).setType(TileType.WALL);
            dungeonMap.getTileAt(4, cnt).setType(TileType.WALL);
        }

        return dungeonMap;
    }
}
