package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.battle.CardResult;
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

        applyEffectTo.addStatusEffect(statusEffect.duplicate());
        statusEffect.effectApplied(applyEffectTo,battleRound);
        String desc = statusEffect.description() + " is applied to {1}";
        if ( statusEffect.isBuff() ) {
            battleRound.addBattleResult(usedBy, CardResult.newBuffResult(desc,applyEffectTo));
        } else {
            battleRound.addBattleResult(usedBy, CardResult.newDebuffResult(desc,applyEffectTo));
        }
    }

    @Override
    public String description() {
        return statusEffect.description();
    }
}
