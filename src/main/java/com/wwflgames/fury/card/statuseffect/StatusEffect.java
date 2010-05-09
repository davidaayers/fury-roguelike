package com.wwflgames.fury.card.statuseffect;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.mob.Mob;

public abstract class StatusEffect {
    private String name;
    private int numRoundsLeft;

    public void roundOccurred(Mob mob, BattleRound battleRound) {
        numRoundsLeft--;
        doRoundOccurred(mob, battleRound);
    }

    public boolean isActive() {
        return numRoundsLeft > 0;
    }

    protected abstract void doRoundOccurred(Mob mob, BattleRound battleRound);

    public abstract void effectApplied(Mob mob, BattleRound battleRound);

    public abstract void effectRemoved(Mob mob, BattleRound battleRound);
}
