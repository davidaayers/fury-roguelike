package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;

public class DeathEffect extends ItemEffect {
    @Override
    public String getDesc() {
        return "Death";
    }

    @Override
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsageResult result) {
        // they are already dead
    }
}
