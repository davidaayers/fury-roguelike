package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemEffectResult;
import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.effect.damage.*;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.util.Log;

import java.util.List;

public class MeleeDamageEffect extends AbstractDamageEffect {

    public MeleeDamageEffect(MeleeDamage damage, int damageAmount) {
        super(damage, damageAmount);
    }

    @Override
    public void applyEffect(Mob itemUser, Mob itemUsedUpon, ItemUsageResult result) {
        if (damage instanceof CrushDamage) {
            applyCrushDamage(itemUser, itemUsedUpon, result);
        } else if (damage instanceof SlashDamage) {
            applySlashDamage(itemUser, itemUsedUpon, result);
        } else if (damage instanceof StabDamage) {
            applyStabDamage(itemUser, itemUsedUpon, result);
        }
    }

    // crush damage gets applied to armor first (and destroys it, at least
    // for this combat round), then to health
    private void applyCrushDamage(Mob itemUser, Mob usedOn, ItemUsageResult result) {

        List<AttackBuffEffect> attackBuffs = EffectHelper.findAndRemoveApplicableBuffs(itemUser, Damage.CRUSH_DAMAGE);

        Log.debug("Got " + attackBuffs + " attack buffs");
        int buffAmt = calculateBuffDamageIncrease(itemUser, result, attackBuffs);
        Log.debug("Total Buff amount = " + buffAmt);

        int armor = usedOn.getBattleStatValue(Stat.ARMOR);
        int healthBefore = usedOn.getStatValue(Stat.HEALTH);
        int armorBefore = armor;

        int dmg = damageAmount + buffAmt;

        // crush damage is increased based on strength. Get a multiplier for
        // strength
        float multiplier = multiplierFor(itemUser, Stat.STRENGTH);
        Log.debug("multiplier was " + multiplier);
        int origDmg = dmg;
        dmg *= multiplier;
        if (origDmg != dmg) {
            String msg = "{0} strength increased the attack by {2}!";
            result.add(ItemEffectResult.newBuffItemEffect(msg, dmg - origDmg, itemUser));
        }


        Log.debug("Armor before: " + armor);
        Log.debug("Dmg before  : " + dmg);

        if (dmg > armor) {
            dmg -= armor;
            armor = 0;
        } else {
            armor -= dmg;
            dmg = 0;
        }

        Log.debug("Armor after : " + armor);
        Log.debug("Dmg after   : " + dmg);

        usedOn.modifyStatValue(Stat.HEALTH, -dmg);
        usedOn.setBattleStatValue(Stat.ARMOR, armor);

        int armorAfter = usedOn.getBattleStatValue(Stat.ARMOR);
        int healthAfter = usedOn.getStatValue(Stat.HEALTH);

        int armorDelta = armorBefore - armorAfter;
        int healthDelta = healthBefore - healthAfter;

        String armorDesc = "{0} armor is crushed for {2}";
        String healthDesc = "{1} takes {2} damage!";
        if (armorBefore != 0 && armorDelta != 0) {
            result.add(ItemEffectResult.newDamageItemEffect(armorDesc, armorDelta, usedOn));
        }
        if (healthDelta != 0) {
            result.add(ItemEffectResult.newDamageItemEffect(healthDesc, healthDelta, usedOn));
        } else {
            result.add(ItemEffectResult.newDamageItemEffect("{0} armor absorbed all damage!", armorDelta, usedOn));
        }
    }

    // slash damage is reduced by 10% for every 10 points of armor. So
    // if the mob has 100 armor or more, they are basically immune to
    // slash damage
    private void applySlashDamage(Mob itemUser, Mob mob, ItemUsageResult result) {

    }

    // not sure how stab damage should work. Ignore armor?
    private void applyStabDamage(Mob itemUser, Mob mob, ItemUsageResult result) {

    }


}
