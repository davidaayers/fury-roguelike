package com.wwflgames.fury.mob;

import com.wwflgames.fury.battle.ItemUsage;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.effect.StatusItemEffect;

public class OldStatusEffect {
    private Mob effectedMob;
    private Item item;
    private StatusItemEffect statusItemEffect;
    private int rounds;

    public OldStatusEffect(Mob effectedMob, Item item, StatusItemEffect statusItemEffect, int rounds) {
        this.effectedMob = effectedMob;
        this.item = item;
        this.statusItemEffect = statusItemEffect;
        this.rounds = rounds;
    }

    public void roundOccurred() {
        rounds --;
    }

    public boolean stillActive() {
        return rounds > 0;
    }

    public void woreOff(ItemUsage result) {
        statusItemEffect.removeEffect(effectedMob,result);
    }

}
