package com.wwflgames.fury.monster.ai;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.monster.Monster;

// A dumb AI that just finds an empty adjacent square and moves to it, unless the
// player is next to the monster, in which case the monster will initiate combat
public class RandomMonsterAI extends AbstractMonsterAI {

    public RandomMonsterAI(Monster monster, DungeonMap dungeonMap) {
        super(monster, dungeonMap);
    }

    @Override
    public void think() {
        
    }
}
