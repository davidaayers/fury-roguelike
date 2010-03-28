package com.wwflgames.fury.monster;

import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.mob.Mob;

public class Monster extends Mob {

    private String spriteSheet;
    private int monsterValue;

    public Monster(String name, String spriteSheet, int monsterValue) {
        super(name);
        this.spriteSheet = spriteSheet;
        this.monsterValue = monsterValue;
    }

    public Monster(Monster other, int monsterValue) {
        this(other.name(), other.getSpriteSheet(), monsterValue);
        setDeck(new ItemDeck(other.getDeck()));
        this.stats.putAll(other.stats);
    }


    public String getSpriteSheet() {
        return spriteSheet;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + name() + '\'' + " " +
                "spriteSheet='" + spriteSheet + '\'' +
                '}';
    }

    public int monsterValue() {
        return monsterValue;
    }
}
