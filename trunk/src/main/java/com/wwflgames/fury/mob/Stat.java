package com.wwflgames.fury.mob;

import java.util.ArrayList;
import java.util.List;

public enum Stat {

    STRENGTH("Strength", false),
    DEXTERITY("Dexterity", false),
    MAGIC("Magic", false),
    HEALTH("Health", false),
    ARMOR("Armor", true),
    WILL("Will", true);

    private String desc;
    private boolean isBattleStat;

    Stat(String desc, boolean battleState) {
        this.desc = desc;
        isBattleStat = battleState;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isBattleStat() {
        return isBattleStat;
    }

    public static List<Stat> battleStats() {
        List<Stat> battleStatList = new ArrayList<Stat>();
        for ( Stat stat : Stat.values() ) {
            if ( stat.isBattleStat() ) {
                battleStatList.add(stat);
            }
        }
        return battleStatList;
    }
}
