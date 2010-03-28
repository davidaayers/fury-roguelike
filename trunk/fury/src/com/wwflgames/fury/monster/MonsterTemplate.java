package com.wwflgames.fury.monster;

import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.mob.Stat;

import java.util.HashMap;
import java.util.Map;

public class MonsterTemplate {
    private String baseName;
    private String spriteSheet;
    private Integer pointsLow;
    private Integer pointsHigh;
    protected Map<Stat, StatRange> stats = new HashMap<Stat, StatRange>();
    protected ItemDeck deck;

    public MonsterTemplate(String baseName, String spriteSheet, Integer pointsLow, Integer pointsHigh) {
        this.baseName = baseName;
        this.spriteSheet = spriteSheet;
        this.pointsLow = pointsLow;
        this.pointsHigh = pointsHigh;
    }

    // given pointsLow and pointsHigh, compute a percent which gives a monster's
    // relative strength level.
    // So, for pointsLow = 1 and pointsHigh = 3,
    // 1 would return 0%
    // 2 would return 50%
    // 3 would return 100%
    //
    // for pointsLow = 1 and pointsHigh = 4
    // 1 would return 0%
    // 2 would return 33%
    // 3 would return 66%
    // 4 would return 100%

    public double relativePctIncrease(int points) {
        int diff = pointsHigh - pointsLow;
        double interval = 1.0d / (double) diff;
        int multiplier = points - 1;
        double pct = interval * multiplier;
        return pct;
    }

    public void setStatRange(Stat stat, StatRange value) {
        stats.put(stat, value);
    }

    public void installStats(Monster monster) {
        int monsterPoints = monster.monsterValue();
        double pct = relativePctIncrease(monsterPoints);
        for (Stat stat : stats.keySet()) {
            StatRange range = stats.get(stat);
            monster.setStatValue(stat, range.valueForPercent(pct));
        }
    }

    public void setDeck(ItemDeck deck) {
        this.deck = deck;
    }

    public boolean matchesPoints(int points) {
        return points >= pointsLow && points <= pointsHigh;
    }

    public Monster createForPoints(int points) {
        Monster m = new Monster(baseName, spriteSheet, points);
        installStats(m);
        m.setDeck(deck);
        return m;
    }
}
