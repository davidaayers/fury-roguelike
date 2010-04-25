package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.item.Item;

public abstract class AbstractItemEffect implements ItemEffect {
    protected Item item;

    @Override
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
