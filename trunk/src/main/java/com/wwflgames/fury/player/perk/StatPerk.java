package com.wwflgames.fury.player.perk;

import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.player.perk.Perk;

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
