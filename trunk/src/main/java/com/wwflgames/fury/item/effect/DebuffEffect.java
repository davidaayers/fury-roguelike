package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.mob.Mob;

public abstract class DebuffEffect extends LimitedUseEffect {
    protected DebuffEffect(int uses) {
        super(uses);
    }

    public abstract void woreOff(Mob usedUpon);
}
