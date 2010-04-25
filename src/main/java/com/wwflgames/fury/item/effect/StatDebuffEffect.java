package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsage;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

public class StatDebuffEffect extends DebuffEffect {

    private Stat stat;
    private int amount;

    public StatDebuffEffect(Stat stat, int amount,int uses) {
        super(uses);
        this.stat = stat;
        this.amount = amount;
    }

    @Override
    public String getDesc() {
        String desc = "" + amount + " " + stat.getDesc();
        if ( uses > 1 ) {
            desc += "("+uses+" rnds)";
        }
        return desc;    }

    @Override
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsage result) {
        itemUser.modifyBattleStatValue(stat, amount);
        // add a message
        String desc = "{0} " + stat.getDesc() + " is decreased by " + Math.abs(amount);
        result.add(ItemEffectResult.newBuffItemEffect(desc, itemUser));
    }

    @Override
    public void woreOff(Mob usedUpon) {

    }
}
