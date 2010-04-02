package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;

public interface Item {
    String name();

    Item usedBy(Mob mob, ItemUsageResult result);

    Item usedAgainst(Mob usedBy, Mob usedAgainst, ItemUsageResult result);
}
