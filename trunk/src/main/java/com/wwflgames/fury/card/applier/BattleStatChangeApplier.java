package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.battle.CardResult;
import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

/**
 * Applies a change (+/-) to a battle stat, can be used
 * to add or remove value. Example uses:
 *
 * - Shield (+ armor)
 * - Protective spell (+ willpower)
 * - Corrosive Acid (- armor)
 */
public class BattleStatChangeApplier implements Applier {

    private Stat battleStat;
    private int changeAmount;
    private boolean applyToUsedBy;

    public BattleStatChangeApplier(Stat battleStat, int changeAmount, boolean applyToUsedBy) {
        this.battleStat = battleStat;
        this.changeAmount = changeAmount;
        this.applyToUsedBy = applyToUsedBy;
    }

    @Override
    public void applyTo(Card card, Mob usedBy, Mob usedAgainst, BattleRound battleRound) {
        if ( applyToUsedBy ) {
            applyChange(card,usedBy,battleRound);
        } else {
            applyChange(card,usedAgainst,battleRound);
        }
    }

    private void applyChange(Card card, Mob mob, BattleRound battleRound) {
        mob.setBattleStatValue(battleStat,mob.getBattleStatValue(battleStat)+changeAmount);
        String desc = "{0} " + battleStat.getDesc().toLowerCase() + "";
        if ( changeAmount > 0 ) {
            desc += " increased ";
        } else {
            desc += " decreased ";
        }
        desc += " by " + Math.abs(changeAmount);
        battleRound.addBattleResult(mob, CardResult.newBuffResult(desc,mob));
    }

    @Override
    public String description() {
        return (changeAmount > 0 ? "+": "") + changeAmount + " to " + battleStat.getDesc();
    }
}
