package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

public class StatBuffEffect extends BuffEffect {

    private Stat stat;
    private int amount;

    public StatBuffEffect(Stat stat, int amount) {
        this.stat = stat;
        this.amount = amount;
    }

    @Override
    public String getDesc() {
        return "+" + amount + " to " + stat.getDesc();
    }

    @Override
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsageResult result) {
        itemUser.modifyBattleStatValue(stat, amount);
        // add a message
        String desc = "{0} " + stat.getDesc() + " is increased by {2}";
        result.add(ItemEffectResult.newBuffItemEffect(desc, amount, itemUser));
    }
}
