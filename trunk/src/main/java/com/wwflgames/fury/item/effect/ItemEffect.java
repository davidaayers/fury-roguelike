package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.mob.Mob;

public abstract class ItemEffect {

    protected Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public abstract String getDesc();

    public abstract void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsageResult result);

}
