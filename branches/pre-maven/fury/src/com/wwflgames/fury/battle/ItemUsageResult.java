package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.List;

public class ItemUsageResult {
    private List<ItemEffectResult> effectResults = new ArrayList<ItemEffectResult>();

    private Item item;
    private Mob mob;

    public ItemUsageResult(Item item, Mob mob) {
        this.item = item;
        this.mob = mob;
    }

    public ItemUsageResult add(ItemEffectResult effectResult) {
        effectResults.add(effectResult);
        return this;
    }

    public ItemUsageResult addAtFront(ItemEffectResult effectResult) {
        effectResults.add(0, effectResult);
        return this;
    }

    public List<ItemEffectResult> get() {
        return effectResults;
    }

    public Item item() {
        return item;
    }

    public Mob mob() {
        return mob;
    }
}
