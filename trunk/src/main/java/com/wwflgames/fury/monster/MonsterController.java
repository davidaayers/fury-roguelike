package com.wwflgames.fury.monster;

import com.wwflgames.fury.map.DungeonMap;

import java.util.List;

public class MonsterController {

    public DungeonMap map;

    public MonsterController(DungeonMap map) {
        this.map = map;
    }

    public void think() {
        for ( Monster monster : map.getMonsterList() ) {
            monster.think();
        }
    }
}
