package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.ItemUsage;
import com.wwflgames.fury.mob.Mob;

public interface Item {
    String name();

    Item usedBy(Mob mob, ItemUsage result);

    Item usedAgainst(Mob usedBy, Mob usedAgainst, ItemUsage result);
}
