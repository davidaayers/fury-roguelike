package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.battle.CardResult;
import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;

/**
 * Applies damage of a given type.
 */
public class DamageApplier implements Applier {

    private DamageType damageType;
    private int damageAmount;

    public DamageApplier(DamageType damageType, int damageAmount) {
        this.damageType = damageType;
        this.damageAmount = damageAmount;
    }

    @Override
    public void applyTo(Card card, Mob usedBy, Mob usedAgainst, BattleRound battleRound) {

        // see if the usedBy mob has any stats that will effect
        // the damage
        Stat[] buffedBy = damageType.getBuffedBy();
        float buffMultiplier = 0f;
        if ( buffedBy != null ) {
            for ( Stat stat : buffedBy ) {
                float increasePct = multiplierFor(usedBy,stat);
                //TODO: add a message about the increase
                String msg = "{0} "+stat.getDesc()+" increased the attack by "+increasePct+"%";
                battleRound.addBattleResult(usedBy, CardResult.newBuffResult(msg,usedBy));
                buffMultiplier += increasePct;
            }
        }

        //TODO: see if usedBy has any StatusEffects applied which effect
        // this type of damage

        int dmg = damageAmount;
        dmg = dmg * (int)( 1f + buffMultiplier );

        //TODO: handle stuff that reduces damage, like armor, or will, or whatever
        usedAgainst.modifyStatValue(Stat.HEALTH, -dmg);

        // add a message about the damage caused
        String healthDesc = "{1} takes "+dmg+" damage!";
        battleRound.addBattleResult(usedBy, CardResult.newDamageResult(healthDesc,usedAgainst));
    }

    protected float multiplierFor(Mob itemUser, Stat stat) {
        int statValue = itemUser.getBattleStatValue(stat);
        return ((float) statValue / 100f);
    }
}
