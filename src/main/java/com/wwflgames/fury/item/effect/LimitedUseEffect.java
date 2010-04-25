package com.wwflgames.fury.item.effect;

public abstract class LimitedUseEffect extends ItemEffect {
    protected int uses;

    protected LimitedUseEffect(int uses) {
        this.uses = uses;
    }

    public void used() {
        uses--;
    }

    public boolean stillActive() {
        return uses > 0;
    }
}
