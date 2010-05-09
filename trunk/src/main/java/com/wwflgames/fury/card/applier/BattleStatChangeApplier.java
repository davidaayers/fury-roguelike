package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.mob.Mob;

/**
 * Applies a change (+/-) to a battle stat, can be used
 * to add or remove value. Example uses:
 *
 * - Shield (+ armor)
 * - Protective spell (+ willpower)
 * - Corrosive Acid (- armor)
 */
public class BattleStatChangeApplier implements Applier {
    @Override
    public void applyTo(Card card, Mob usedBy, Mob usedAgainst, BattleRound battleRound) {

    }
}
