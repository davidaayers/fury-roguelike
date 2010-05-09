package com.wwflgames.fury.battle;

import com.wwflgames.fury.mob.Mob;

public class CardResult extends BattleResult {

    public CardResult(ResultType resultType, Mob effectedMob, String desc) {
        super(resultType, effectedMob, desc);
    }

    public static CardResult newDamageResult(String desc, Mob effectedMob) {
        return new CardResult(ResultType.DAMAGE,effectedMob,desc);
    }

    public static CardResult newBuffResult(String desc, Mob effectedMob) {
        return new CardResult(ResultType.BUFF,effectedMob,desc);
    }

}
