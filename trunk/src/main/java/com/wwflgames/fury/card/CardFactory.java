package com.wwflgames.fury.card;

import com.wwflgames.fury.card.applier.Applier;
import com.wwflgames.fury.card.applier.BattleStatChangeApplier;
import com.wwflgames.fury.card.applier.DamageApplier;
import com.wwflgames.fury.card.applier.DamageType;
import com.wwflgames.fury.mob.Stat;

public class CardFactory {
    public Card getCardByName(String name) {
        return newDamageCard(name,DamageType.CRUSH,5);
    }

    public Card getBuffCardByName(String name) {
        return newBuffCard(name);
    }

    private Card newBuffCard(String name) {
        BattleStatChangeApplier bsc = new BattleStatChangeApplier(Stat.ARMOR,10,true);
        return new Card(name,new Applier[] { bsc } , null , 0 );
    }

    private Card newDamageCard(String name, DamageType damageType, int damageAmt) {
        DamageApplier da = new DamageApplier(damageType,damageAmt);
        return new Card(name,null,new Applier[] { da } , 1);
    }

}
