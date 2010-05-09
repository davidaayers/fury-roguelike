package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.card.statuseffect.StatusEffect;
import com.wwflgames.fury.mob.Mob;

/**
 * Applies a status effect to the given mob.
 */
public class StatusEffectApplier implements Applier {

    private StatusEffect statusEffect;
    // applyToSelf == true means applyTo "usedBy"
    // applyToSelf == false means applyTo "usedAgainst"
    private boolean applyToSelf = false;

    public StatusEffectApplier(StatusEffect statusEffect, boolean applyToSelf) {
        this.statusEffect = statusEffect;
        this.applyToSelf = applyToSelf;
    }

    @Override
    public void applyTo(Card card, Mob usedBy, Mob usedAgainst, BattleRound battleRound) {

        Mob applyEffectTo;

        if ( applyToSelf ) {
            applyEffectTo = usedBy;
        } else {
            applyEffectTo = usedAgainst;
        }

        // put a clone of it here? probably
        applyEffectTo.addStatusEffect(statusEffect);
        statusEffect.effectApplied(applyEffectTo,battleRound);
    }
}
