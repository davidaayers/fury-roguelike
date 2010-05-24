package com.wwflgames.fury.card;

import com.wwflgames.fury.card.applier.Applier;
import com.wwflgames.fury.card.applier.DamageApplier;
import com.wwflgames.fury.card.applier.DamageType;

public class CardFactory {
    public Card getCardByName(String name) {
        return newDamageCard(name,DamageType.CRUSH,5);
    }

    private Card newDamageCard(String name, DamageType damageType, int damageAmt) {
        DamageApplier da = new DamageApplier(damageType,damageAmt);
        return new Card(name,null,new Applier[] { da } , 1);
    }

}
