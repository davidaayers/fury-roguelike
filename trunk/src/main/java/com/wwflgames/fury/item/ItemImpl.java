package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.ItemUsage;
import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.mob.Mob;

public class ItemImpl implements Item {

    private String name;
    private ItemEffect[] usedByEffects;
    private ItemEffect[] usedAgainstEffects;

    public ItemImpl(String name, ItemEffect[] usedByEffects, ItemEffect[] usedAgainstEffects) {
        this.name = name;
        this.usedByEffects = usedByEffects;
        this.usedAgainstEffects = usedAgainstEffects;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Item usedBy(Mob usedBy, ItemUsage result) {
        if (usedByEffects != null) {
            applyEffects(usedByEffects, usedBy, null, result);
        }
        return this;
    }

    @Override
    public Item usedAgainst(Mob usedBy, Mob usedAgainst, ItemUsage result) {
        if (usedAgainstEffects != null) {
            applyEffects(usedAgainstEffects, usedBy, usedAgainst, result);
        }
        return this;
    }

    private void applyEffects(ItemEffect[] effects, Mob usedBy, Mob usedAgainst, ItemUsage result) {
        for (ItemEffect effect : effects) {
            effect.applyEffect(usedBy, usedAgainst, result);
        }
    }

    public ItemEffect[] getUsedByEffects() {
        return usedByEffects;
    }

    public ItemEffect[] getUsedAgainstEffects() {
        return usedAgainstEffects;
    }

    @Override
    public String toString() {
        return "ItemImpl: " + name;
    }
}
