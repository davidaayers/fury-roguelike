package com.wwflgames.fury.battle;

import com.wwflgames.fury.mob.Mob;

public abstract class BattleResult {

    private ResultType resultType;
    private Mob effectedMob;
    private String desc;

    public BattleResult(ResultType resultType, Mob effectedMob, String desc) {
        this.resultType = resultType;
        this.effectedMob = effectedMob;
        this.desc = desc;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public Mob getEffectedMob() {
        return effectedMob;
    }

    public String getDesc() {
        return desc;
    }
}
