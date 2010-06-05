package com.wwflgames.fury.card.statuseffect;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.battle.CardResult;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

/**
 * DOT = Damage Over Time. Applied an amount of damage each round that
 * the status effect is active
 */
public class DotStatusEffect extends StatusEffect {

    private int dmgPerRound;

    public DotStatusEffect(String name, int numRounds, int dmgPerRound) {
        super(name, numRounds,false);
        this.dmgPerRound = dmgPerRound;
    }

    @Override
    public StatusEffect duplicate() {
        return new DotStatusEffect(this.name,this.numRoundsLeft,this.dmgPerRound);
    }

    @Override
    protected void doRoundOccurred(Mob mob, BattleRound battleRound) {
        mob.modifyStatValue(Stat.HEALTH, -dmgPerRound);
        String desc = "{1} takes " + dmgPerRound + " damage from " + name;
        battleRound.addBattleResult(mob, CardResult.newDamageResult(desc,mob));
    }

    @Override
    public void effectApplied(Mob mob, BattleRound battleRound) {

    }

    @Override
    public void effectRemoved(Mob mob, BattleRound battleRound) {
        String desc = name + " wore off";
        battleRound.addBattleResult(mob, CardResult.newDamageResult(desc,mob));
    }
}
