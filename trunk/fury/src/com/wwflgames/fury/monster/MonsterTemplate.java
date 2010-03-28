package com.wwflgames.fury.monster;

import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Shuffler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonsterTemplate {
    private String baseName;
    private String spriteSheet;
    private Integer pointsLow;
    private Integer pointsHigh;
    protected Map<Stat, StatRange> stats = new HashMap<Stat, StatRange>();
    protected ItemDeck deck;
    protected Map<Integer, List<String>> preModifiers = new HashMap<Integer, List<String>>();

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
        if (diff == 0) {
            return 0d;
        }
        double interval = 1.0d / (double) diff;
        int multiplier = points - 1;
        double pct = interval * multiplier;
        return pct;
    }

    public void setStatRange(Stat stat, StatRange value) {
        stats.put(stat, value);
    }

    public void addPreModifier(Integer points, String modifier) {
        List<String> modifiers = preModifiers.get(points);
        if (modifiers == null) {
            modifiers = new ArrayList<String>();
            preModifiers.put(points, modifiers);
        }
        modifiers.add(modifier);
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
        Monster m = new Monster(chooseName(points), spriteSheet, points);
        installStats(m);
        m.setDeck(deck);
        return m;
    }

    private String chooseName(int points) {
        List<String> preModifiersList = preModifiers.get(points);
        String name = baseName;
        if (preModifiersList != null && !preModifiersList.isEmpty()) {
            Shuffler.shuffle(preModifiersList);
            String pre = preModifiersList.get(0);
            if (!pre.equals("")) {
                name = pre + " " + name;
            }
        }
        return name;
    }
}
