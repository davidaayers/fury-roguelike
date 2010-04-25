package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EffectHelper {

    public static List<AttackBuffEffect> findAndRemoveApplicableBuffs(Mob mob, Damage damageType) {
        List<AttackBuffEffect> applicableBuffs = new ArrayList<AttackBuffEffect>();
        List<BuffEffect> buffEffects = mob.getBuffs();
        Log.debug("Buff effects for " + mob + " were " + buffEffects);
        for (BuffEffect buff : buffEffects) {
            Log.debug("Looking at buff " + buff);
            if (buff instanceof AttackBuffEffect) {
                AttackBuffEffect attackBuff = (AttackBuffEffect) buff;
                Log.debug("attackBuff = " + attackBuff.getDesc());

                if (attackBuff.getDamage().getClass().isAssignableFrom(damageType.getClass())) {
                    Log.debug("It's assignable");
                    applicableBuffs.add(attackBuff);
                }

            }
        }
        // now, remove the buffs from the mob if they aren't active anymore after being used
        for (AttackBuffEffect buff : applicableBuffs) {
            buff.used();
            if ( !buff.stillActive() ) {
                mob.removeBuff(buff);
            }
        }
        return applicableBuffs;
    }
}
