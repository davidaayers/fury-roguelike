package com.wwflgames.fury.battle;

import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleRoundResult {

    private Map<Mob, ItemUsageResult> mobItemUsageResults = new HashMap<Mob, ItemUsageResult>();
    private int round;

    public BattleRoundResult(int round) {
        this.round = round;
    }

    public void addItemUsageResultFor(Mob mob, ItemUsageResult result) {
        mobItemUsageResults.put(mob, result);
    }

    public ItemUsageResult getItemUsageResultFor(Mob mob) {
        return mobItemUsageResults.get(mob);
    }

    public List<Monster> getMonsters() {
        List<Monster> monsters = new ArrayList<Monster>();
        for (Mob mob : mobItemUsageResults.keySet()) {
            if (mob instanceof Monster) {
                monsters.add((Monster) mob);
            }
        }
        return monsters;
    }


}
