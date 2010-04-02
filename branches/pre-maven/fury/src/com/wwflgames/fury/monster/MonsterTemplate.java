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
    protected Map<MonsterLevel, List<String>> preModifiers = new HashMap<MonsterLevel, List<String>>();
    protected Map<MonsterLevel, List<String>> postModifiers = new HashMap<MonsterLevel, List<String>>();
    protected Map<String, ItemDeck> decks = new HashMap<String, ItemDeck>();

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

    public void addNameModifier(String type, MonsterLevel level, String modifier) {
        if ("pre".equals(type)) {
            addModifier(preModifiers, level, modifier);
        } else {
            addModifier(postModifiers, level, modifier);
        }
    }

    private void addModifier(Map<MonsterLevel, List<String>> modifiersMap, MonsterLevel level, String modifier) {
        List<String> modifiers = modifiersMap.get(level);
        if (modifiers == null) {
            modifiers = new ArrayList<String>();
            modifiersMap.put(level, modifiers);
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

    public void addDeck(String level, ItemDeck deck) {
        decks.put(level, deck);
    }

    public boolean matchesPoints(int points) {
        return points >= pointsLow && points <= pointsHigh;
    }

    public Monster createForLevel(MonsterLevel level) {
        Monster m = new Monster(chooseName(level), spriteSheet, level.getLevel());
        installStats(m);

        // create the deck for this monster.
        // we start with the default deck
        ItemDeck monsterDeck = new ItemDeck();
        ItemDeck itemDeck = decks.get("default");
        monsterDeck.addAllItems(itemDeck.getDeck());

        // see if there are level specific cards to be added to the deck
        ItemDeck levelDeck = decks.get(Integer.toString(level.getLevel()));
        if (levelDeck != null) {
            monsterDeck.addAllItems(levelDeck.getDeck());
        }

        // finally, see if this is a boss, and add the boss items
        if (level.isBoss()) {
            ItemDeck bossDeck = decks.get("boss");
            if (bossDeck != null) {
                monsterDeck.addAllItems(bossDeck.getDeck());
            }
            m.setBoss(true);
        }


        m.setDeck(monsterDeck);
        return m;
    }

    private String chooseName(MonsterLevel level) {
        String name = baseName;

        List<String> preModifiersList = preModifiers.get(level);
        if (preModifiersList != null && !preModifiersList.isEmpty()) {
            Shuffler.shuffle(preModifiersList);
            String pre = preModifiersList.get(0);
            if (!pre.equals("")) {
                name = pre + " " + name;
            }
        }

        List<String> postModifiersList = postModifiers.get(level);
        if (postModifiersList != null && !postModifiersList.isEmpty()) {
            Shuffler.shuffle(postModifiersList);
            String post = postModifiersList.get(0);
            if (!post.equals("")) {
                name = name + " " + post;
            }
        }

        return name;
    }

    public Integer getPointsLow() {
        return pointsLow;
    }

    public Integer getPointsHigh() {
        return pointsHigh;
    }
}
