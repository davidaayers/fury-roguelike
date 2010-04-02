package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.mob.Mob;

public class ItemEffectResult {
    private String desc;
    private Integer delta;
    private Mob effectedMob;
    private ItemEffect effect;

    public ItemEffectResult(String desc, Mob effectedMob, ItemEffect effect) {
        this.desc = desc;
        this.effectedMob = effectedMob;
        this.effect = effect;
    }

    public ItemEffectResult(String desc, Integer delta, Mob effectedMob, ItemEffect effect) {
        this.desc = desc;
        this.delta = delta;
        this.effectedMob = effectedMob;
        this.effect = effect;
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

    public ItemEffect getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        return "ItemEffectResult{" +
                "desc='" + desc + '\'' +
                ", delta=" + delta +
                '}';
    }
}
