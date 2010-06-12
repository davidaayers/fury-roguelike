package com.wwflgames.fury.player;

import com.wwflgames.fury.mob.Stat;

public class StatPerk extends Perk {

    private Stat stat;
    private int amount;

    public StatPerk(String name, String description, Perk prerequisitePerk, Stat stat, int amount) {
        super(name, description, prerequisitePerk);
        this.stat = stat;
        this.amount = amount;
    }

    @Override
    public void applyPerk(Player player) {
        player.setStatValue(stat,player.getStatValue(stat)+amount);
    }
}
