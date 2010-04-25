package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsage;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

public class StatBuffEffect extends BuffEffect {

    private Stat stat;
    private int amount;

    public StatBuffEffect(Stat stat, int amount) {
        // stat buffs are always 1-use
        super(1);
        this.stat = stat;
        this.amount = amount;
    }

    @Override
    public String getDesc() {
        return "+" + amount + " to " + stat.getDesc();
    }

    @Override
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsage result) {
        itemUser.modifyBattleStatValue(stat, amount);
        // add a message
        String desc = "{0} " + stat.getDesc() + " is increased by " + amount;
        result.add(ItemEffectResult.newBuffItemEffect(desc, itemUser));
    }

    public void removeEffect(Mob effectedMob) {
        // reverse the buff
        effectedMob.modifyBattleStatValue(stat,-amount);

        //TODO: create some kind of message
    }
}
