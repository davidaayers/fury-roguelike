package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.item.effect.damage.MeleeDamage;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.player.Profession;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class MeleeDamageEffectTest {

    @Test
    public void findAndRemoveApplicableBuffsWithSameDamageType() throws Exception {
        MeleeDamageEffect damageEffect = new MeleeDamageEffect((MeleeDamage) Damage.CRUSH_DAMAGE, 8);
        Profession profession = null;
        Player player = new Player("foo", profession);
        Monster monster = new Monster("bar", "sprite-sheet", 0);

        // add a buff to the player
        AttackBuffEffect effect = new AttackBuffEffect(Damage.CRUSH_DAMAGE, 8,1);
        player.addBuff(effect);

        List<AttackBuffEffect> foundBuffs = EffectHelper.findAndRemoveApplicableBuffs(player, Damage.CRUSH_DAMAGE);
        assertEquals(1, foundBuffs.size());
        assertEquals(effect, foundBuffs.get(0));
    }

    @Test
    public void testFindAndRemoveApplicableBuffsWithSubclassDamageType() throws Exception {
        MeleeDamageEffect damageEffect = new MeleeDamageEffect((MeleeDamage) Damage.CRUSH_DAMAGE, 8);
        Profession profession = null;
        Player player = new Player("foo", profession);
        Monster monster = new Monster("bar", "sprite-sheet", 0);

        // add a buff to the player
        AttackBuffEffect effect = new AttackBuffEffect(Damage.MELEE_DAMAGE, 8,1);
        player.addBuff(effect);

        List<AttackBuffEffect> foundBuffs = EffectHelper.findAndRemoveApplicableBuffs(player, Damage.CRUSH_DAMAGE);
        assertEquals(1, foundBuffs.size());
        assertEquals(effect, foundBuffs.get(0));
    }

}
