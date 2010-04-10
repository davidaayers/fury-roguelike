package com.wwflgames.fury.monster.ai;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.util.Shuffler;

import java.util.ArrayList;
import java.util.List;

// A dumb AI that just finds an empty adjacent square and moves to it, unless the
// player is next to the monster, in which case the monster will initiate combat
public class RandomMonsterAI extends AbstractMonsterAI {

    public RandomMonsterAI(Monster monster, DungeonMap dungeonMap) {
        super(monster, dungeonMap);
    }

    @Override
    public void think() {

        int monsterX = monster.getMapX();
        int monsterY = monster.getMapY();

        List<Tile> adjacentTiles = new ArrayList<Tile>();
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                int mx = monsterX + x;
                int my = monsterY + y;
                if ( dungeonMap.inBounds(mx, my) ) {
                    Tile tile = dungeonMap.getTileAt(mx, my);
                    if (tile.isWalkable() && tile.getMob() == null ) {
                        adjacentTiles.add(tile);
                    }
                }
            }
        }

        if ( adjacentTiles.isEmpty() ) {
            return;
        }

        // choose one of them
        Shuffler.shuffle(adjacentTiles);

        Tile newLocation = adjacentTiles.get(0);

        dungeonMap.moveMonster(monster,newLocation);
    }
}
