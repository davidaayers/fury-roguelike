package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;

public interface ItemEffect {
    String getDesc();

    void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsageResult result);
}
