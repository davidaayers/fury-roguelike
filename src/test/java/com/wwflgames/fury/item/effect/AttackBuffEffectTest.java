package com.wwflgames.fury.item.effect;

import com.wwflgames.fury.battle.ItemUsage;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.player.Profession;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AttackBuffEffectTest {

    @Test
    public void shouldAddBuffToMob() throws Exception {
        Profession profession = null;
        Player player = new Player("foo", profession);
        Monster monster = new Monster("bar", "sprite-sheet", 0);

        AttackBuffEffect effect = new AttackBuffEffect(Damage.MELEE_DAMAGE, 8,1);
        Item item = null;
        ItemUsage result = new ItemUsage(item, player);
        effect.applyEffect(player, monster, result);

        assertEquals(1, player.getBuffs().size());
        assertTrue(monster.getBuffs().isEmpty());
    }

}
