package com.wwflgames.fury.card.statuseffect;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.mob.Mob;

public abstract class StatusEffect implements Cloneable {
    protected String name;
    protected int numRoundsLeft;
    private boolean isBuff;

    protected StatusEffect(String name,int numRounds,boolean isBuff) {
        this.name = name;
        this.numRoundsLeft = numRounds;
        this.isBuff = isBuff;
    }

    public void roundOccurred(Mob mob, BattleRound battleRound) {
        numRoundsLeft--;
        doRoundOccurred(mob, battleRound);
    }

    public boolean isActive() {
        return numRoundsLeft > 0;
    }

    public String description() {
        return name;
    }

    public boolean isBuff() {
        return isBuff;
    }

    public abstract StatusEffect duplicate();

    protected abstract void doRoundOccurred(Mob mob, BattleRound battleRound);

    public abstract void effectApplied(Mob mob, BattleRound battleRound);

    public abstract void effectRemoved(Mob mob, BattleRound battleRound);
}
