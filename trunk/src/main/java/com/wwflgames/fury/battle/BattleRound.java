package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleRound {
    
    private int round;
    private Map<Mob, Item> itemUsedByMap = new HashMap<Mob,Item>();
    private Map<Mob, List<BattleResult>> battleResultsByMob = new HashMap<Mob,List<BattleResult>>();

    public BattleRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void addItemUsedBy(Mob mob, Item item) {
        itemUsedByMap.put(mob,item);
    }

    public Item getItemUsedBy(Mob mob) {
        return itemUsedByMap.get(mob);
    }

    public void addBattleResult(Mob mob, BattleResult result) {
        List<BattleResult> results = battleResultsByMob.get(mob);
        if ( results == null ) {
            results = new ArrayList<BattleResult>();
            battleResultsByMob.put(mob,results);
        }
        results.add(result);
    }

    public List<BattleResult> getResultsFor(Mob mob) {
        return battleResultsByMob.get(mob);
    }
}
