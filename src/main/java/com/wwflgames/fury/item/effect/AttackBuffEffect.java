package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.mob.Mob;

public class AttackBuffEffect extends BuffEffect {

    private Damage damage;
    private int amount;
    private int numAttacks;

    public AttackBuffEffect(Damage damage, int amount, int numAttacks) {
        this.damage = damage;
        this.amount = amount;
        this.numAttacks = numAttacks;
    }

    @Override
    public String getDesc() {
        return "+" + amount + " " + damage.getType() + " damage";
    }

    @Override
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsageResult result) {
        // add the buff to the itemUser, and report it
        itemUser.addBuff(this);
        String desc = "{0} next ";
        if ( numAttacks > 1 ) {
            desc += numAttacks + " ";
        }
        desc += damage.getType() + " ";
        if ( numAttacks > 1 ) {
            desc += "attacks are ";
        } else {
            desc += "attack is ";
        }
        desc += "increased by {2}";
        result.add(ItemEffectResult.newBuffItemEffect(desc, amount, itemUser));
    }

    public Damage getDamage() {
        return damage;
    }

    public int getAmount() {
        return amount;
    }

    public void used() {
        numAttacks--;
    }

    public boolean stillActive() {
        return numAttacks != 0;
    }


}
