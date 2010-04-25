package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.mob.Mob;

public abstract class DebuffEffect extends LimitedUseEffect {

    private boolean appliesEveryRound = false;

    protected DebuffEffect(int uses, boolean appliesEveryRound) {
        super(uses);
        this.appliesEveryRound = appliesEveryRound;
    }

    public boolean appliesEveryRound() {
        return appliesEveryRound;
    }

    public abstract ItemEffectResult woreOff(Mob usedUpon);
}
