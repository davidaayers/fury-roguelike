package com.wwflgames.fury.battle;

import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleRound {
    
    private int round;
    private Map<Mob, Card> cardsPlayedByMap = new HashMap<Mob,Card>();
    private Map<Mob, List<BattleResult>> battleResultsByMob = new HashMap<Mob,List<BattleResult>>();

    public BattleRound(int round) {
        this.round = round;
    }

    public void addCardPlayedBy(Mob mob, Card card) {
        addBattleResult(mob,CardResult.newCardPlayedResult("{1} used " + card.getName(),mob));
        cardsPlayedByMap.put(mob,card);
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
