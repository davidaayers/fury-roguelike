package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsage;
import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.mob.Mob;

public class AttackBuffEffect extends BuffEffect {

    private Damage damage;
    private int amount;

    public AttackBuffEffect(Damage damage, int amount, int numAttacks) {
        super(numAttacks);
        this.damage = damage;
        this.amount = amount;
    }

    @Override
    public String getDesc() {
        String desc = "+" + amount + " " + damage.getType() + " dmg";
        if ( uses > 1 ) {
            desc += "("+uses+"X)";
        }
        return desc;
    }

    @Override
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsage result) {
        // add the buff to the itemUser, and report it
        itemUser.addBuff(this);
        String desc = "{0} next ";
        if ( uses > 1 ) {
            desc += uses + " ";
        }
        desc += damage.getType() + " ";
        if ( uses > 1 ) {
            desc += "attacks are ";
        } else {
            desc += "attack is ";
        }
        desc += "increased by " + amount;
        result.add(ItemEffectResult.newBuffItemEffect(desc, itemUser));
    }

    public Damage getDamage() {
        return damage;
    }

    public int getAmount() {
        return amount;
    }




}
