package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.mob.Mob;

public class ItemEffectResult extends BattleResult {

    public ItemEffectResult(ResultType resultType, Mob effectedMob, String desc) {
        super(resultType, effectedMob, desc);
    }

    // factory methods
    public static ItemEffectResult newBuffItemEffect(String desc, Mob effectedMob) {
        return new ItemEffectResult(ResultType.BUFF,effectedMob,desc);
    }

    public static ItemEffectResult newDebuffItemEffect(String desc, Mob effectedMob) {
        return new ItemEffectResult(ResultType.DEBUFF,effectedMob,desc);
    }


    public static ItemEffectResult newDamageItemEffect(String desc, Mob effectedMob) {
        return new ItemEffectResult(ResultType.DAMAGE,effectedMob,desc);
    }

    public static ItemEffectResult newDeathItemEffect(String desc, Mob effectedMob) {
        return new ItemEffectResult(ResultType.DEATH,effectedMob,desc);
    }

}
