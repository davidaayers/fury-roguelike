package com.wwflgames.fury.mob;

public enum Stat {

    STRENGTH("Strength", false),
    DEXTERITY("Dexterity", false),
    MAGIC("Magic", false),
    HEALTH("Health", false),
    ARMOR("Armor", true),
    WILL("Will", true);

    private String desc;
    private boolean isBattleState;

    Stat(String desc, boolean battleState) {
        this.desc = desc;
        isBattleState = battleState;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isBattleState() {
        return isBattleState;
    }
}
