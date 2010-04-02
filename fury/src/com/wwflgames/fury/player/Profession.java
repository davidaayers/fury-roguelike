package com.wwflgames.fury.player;

import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.mob.StatHolder;

import java.util.HashMap;
import java.util.Map;

public class Profession implements StatHolder {
    private String name;
    private String spriteSheet;
    private ItemDeck starterDeck;
    private Map<Stat, Integer> starterStats = new HashMap<Stat, Integer>();

    public Profession(String name, String spriteSheet, ItemDeck starterDeck) {
        this.name = name;
        this.spriteSheet = spriteSheet;
        this.starterDeck = starterDeck;
    }

    public String getName() {
        return name;
    }

    public String getSpriteSheet() {
        return spriteSheet;
    }

    public ItemDeck getStarterDeck() {
        return starterDeck;
    }

    public void setStatValue(Stat stat, Integer value) {
        starterStats.put(stat, value);
    }

    public void installStarterStatsOnPlayer(Player player) {
        for (Stat stat : starterStats.keySet()) {
            player.setStatValue(stat, starterStats.get(stat));
        }
    }

    @Override
    public String toString() {
        return "Profession{" +
                "name='" + name + '\'' +
                ", spriteSheet='" + spriteSheet + '\'' +
                '}';
    }
}
