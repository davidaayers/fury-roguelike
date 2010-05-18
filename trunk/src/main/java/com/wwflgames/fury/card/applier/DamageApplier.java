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
                if ( increasePct > 0 ) {
                    String msg = "{0} "+stat.getDesc()+" increased the attack by "+Math.round(increasePct*100)+"%";
                    battleRound.addBattleResult(usedBy, CardResult.newBuffResult(msg,usedBy));
                    buffMultiplier += increasePct;
                }
            }
        }

        int dmg = damageAmount;
        float mult = 1f + buffMultiplier;
        dmg = (int)(dmg * mult);

        //TODO: see if usedBy has any StatusEffects applied which effect
        // this type of damage

        DamageMitigator dm = damageType.getDamageMitigator();
        if ( dm != null ) {
            // first, see if the usedAgainst has any of the stat that provides mitigation
            Stat mitigatingStat = dm.getMitigatingStat();
            int mitigationStatValue = usedAgainst.getBattleStatValue(mitigatingStat);
            if ( mitigationStatValue > 0 ) {
                // mitigate the damage as appropriate
                int dmgBefore = dmg;
                // the mitigation only gos as high as the stat value
                int damageThatCantBeMitigated = dmg - mitigationStatValue;
                // take this off of dmg, it will be added back later
                if ( damageThatCantBeMitigated > 0 ) {
                    dmg = dmg - damageThatCantBeMitigated;
                }
                int totalDmgMitigated = (int)(dmg * dm.getAbsorbedPct());
                dmg = dmg - totalDmgMitigated;
                // add back that which cannot be mitigated
                dmg += damageThatCantBeMitigated;

                if ( totalDmgMitigated > 0 ) {
                    String armorDesc = "{0} "+mitigatingStat.getDesc()+" absorbed " +totalDmgMitigated + " damage";
                    battleRound.addBattleResult(usedBy,CardResult.newDamageResult(armorDesc,usedAgainst));
                }

                // if the mitigator destroys, destroy the mitigating stat
                if ( dm.isDestroyed() ) {
                    int totalDestroyed = (int)(totalDmgMitigated * dm.getDestroyedPct());
                    int newStatValue = mitigationStatValue - totalDestroyed;
                    if ( newStatValue < 0 ) {
                        newStatValue = 0;
                    }
                    usedAgainst.setBattleStatValue(mitigatingStat,newStatValue);
                    String armorDesc = "{0} "+mitigatingStat.getDesc()+" is reduced by " +totalDestroyed;
                    battleRound.addBattleResult(usedBy,CardResult.newDamageResult(armorDesc,usedAgainst));
                }

                // add some messages about the mitigation
                if ( dmg <= 0 ) {
                    // all dmg was absorbed!
                    String msg = "{0} "+mitigatingStat.getDesc()+" reduced damage to zero";
                    battleRound.addBattleResult(usedBy,CardResult.newDamageResult(msg,usedAgainst));
                }
            }
        }

        // cause damage, if there is any left after mitigation
        if ( dmg > 0 ) {
            usedAgainst.modifyStatValue(Stat.HEALTH, -dmg);
            // add a message about the damage caused
            String healthDesc = "{1} takes "+dmg+" damage";
            battleRound.addBattleResult(usedBy, CardResult.newDamageResult(healthDesc,usedAgainst));
        }

    }

    protected float multiplierFor(Mob itemUser, Stat stat) {
        int statValue = itemUser.getBattleStatValue(stat);
        return ((float) statValue / 100f);
    }
}
