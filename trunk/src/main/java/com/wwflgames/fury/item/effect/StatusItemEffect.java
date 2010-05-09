package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsage;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.OldStatusEffect;
import com.wwflgames.fury.mob.Stat;

// applies a status effect to the effected mob that will last the given number
// of rounds.
public abstract class StatusItemEffect extends ItemEffect {

    protected int rounds;
    private Stat stat;
    private int buffAmount;

    protected StatusItemEffect(int rounds, Stat stat, int buffAmount) {
        this.rounds = rounds;
        this.stat = stat;
        this.buffAmount = buffAmount;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    // buffs are applied to the itemUser
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsage result) {

        // apply the effect
        itemUser.modifyBattleStatValue(stat, buffAmount);

        // add a message
        String desc = "{0} " + stat.getDesc() + " is ";
        if ( buffAmount > 0 ) {
            desc += "increased";
        } else {
            desc += "decreased";
        }
        desc += " by " + Math.abs(buffAmount);
        result.add(ItemEffectResult.newBuffItemEffect(desc, itemUser));

        // add the buff to the mob
        OldStatusEffect effect = new OldStatusEffect(itemUser,item,this,rounds);
        itemUsedUpon.addStatusEffect(effect);
    }

    public void removeEffect(Mob mob, ItemUsage result) {
        // reverse the effect
        int reverseBuffAmount = -1 * buffAmount;
        mob.modifyBattleStatValue(stat, reverseBuffAmount);

        // add a message
        String desc = "{0} " + stat.getDesc() + " is ";
        if ( reverseBuffAmount > 0 ) {
            desc += "increased";
        } else {
            desc += "decreased";
        }
        desc += " by " + Math.abs(buffAmount);
        result.add(ItemEffectResult.newBuffItemEffect(desc, mob));
    }

}
