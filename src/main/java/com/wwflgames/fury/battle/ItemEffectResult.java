package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.mob.Mob;

public class ItemEffectResult {

    public enum EffectType {
        DAMAGE,
        BUFF,
        DEBUFF,
        DEATH
    }

    private String desc;
    private Integer delta;
    private Mob effectedMob;
    private EffectType effectType;

    public ItemEffectResult(String desc, Integer delta, Mob effectedMob, EffectType effectType) {
        this.desc = desc;
        this.delta = delta;
        this.effectedMob = effectedMob;
        this.effectType = effectType;
    }

    // factory methods
    public static ItemEffectResult newBuffItemEffect(String desc, Integer delta, Mob effectedMob) {
        return new ItemEffectResult(desc,delta,effectedMob,EffectType.BUFF);
    }

    public static ItemEffectResult newDebuffItemEffect(String desc, Integer delta, Mob effectedMob) {
        return new ItemEffectResult(desc,delta,effectedMob,EffectType.DEBUFF);
    }


    public static ItemEffectResult newDamageItemEffect(String desc, Integer delta, Mob effectedMob) {
        return new ItemEffectResult(desc,delta,effectedMob,EffectType.DAMAGE);
    }

    public static ItemEffectResult newDeathItemEffect(String desc, Mob effectedMob) {
        return new ItemEffectResult(desc,null,effectedMob,EffectType.DEATH);
    }


    public String getDesc() {
        return desc;
    }

    public Integer getDelta() {
        return delta;
    }

    public Mob getEffectedMob() {
        return effectedMob;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    @Override
    public String toString() {
        return "ItemEffectResult{" +
                "desc='" + desc + '\'' +
                ", delta=" + delta +
                '}';
    }
}
