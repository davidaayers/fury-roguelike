package com.wwflgames.fury.monster;

public class MonsterLevel {
    private int level;
    private boolean isBoss;

    public MonsterLevel(int level, boolean boss) {
        this.level = level;
        isBoss = boss;
    }

    public int getLevel() {
        return level;
    }

    public boolean isBoss() {
        return isBoss;
    }
}
