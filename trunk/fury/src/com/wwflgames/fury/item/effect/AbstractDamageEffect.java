package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

import java.util.List;

public abstract class AbstractDamageEffect implements ItemEffect {
    protected Damage damage;
    protected int damageAmount;

    public AbstractDamageEffect(Damage damage, int damageAmount) {
        this.damage = damage;
        this.damageAmount = damageAmount;
    }

    @Override
    public String getDesc() {
        return "" + damageAmount + " " + damage.getType() + " dmg";
    }

    protected float multiplierFor(Mob itemUser, Stat stat) {
        int statValue = itemUser.getStatValue(stat);
        return 1f + ((float) statValue / 100f);
    }

    protected int calculateBuffDamageIncrease(Mob mob, ItemUsageResult result, List<AttackBuffEffect> attackBuffs) {
        int buffAmt = 0;
        for (AttackBuffEffect effect : attackBuffs) {
            buffAmt += effect.getAmount();
            // add message about the buff
            String msg = "{1} attack is increased by {2}!";
            result.add(new ItemEffectResult(msg, effect.getAmount(), mob, effect));
        }
        return buffAmt;
    }
}
