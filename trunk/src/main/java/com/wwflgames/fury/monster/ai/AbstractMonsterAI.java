package com.wwflgames.fury.monster.ai;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.monster.Monster;

public abstract class AbstractMonsterAI implements AI {

    protected Monster monster;
    protected DungeonMap dungeonMap;

    public AbstractMonsterAI(Monster monster, DungeonMap dungeonMap) {
        this.monster = monster;
        this.dungeonMap = dungeonMap;
    }

}
