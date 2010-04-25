package com.wwflgames.fury.item;

import com.wwflgames.fury.battle.ItemUsageResult;
import com.wwflgames.fury.item.effect.AttackBuffEffect;
import com.wwflgames.fury.item.effect.ItemEffect;
import com.wwflgames.fury.item.effect.damage.Damage;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.player.Profession;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ItemImplTest {

    @Test
    public void attackBuffItemAppliesBuffToItemUser() throws Exception {
        Profession profession = null;
        Player player = new Player("foo", profession);
        Monster monster = new Monster("bar", "sprite-sheet", 0);
        AttackBuffEffect effect = new AttackBuffEffect(Damage.MELEE_DAMAGE, 8,1);
        ItemImpl item = new ItemImpl("foo", new ItemEffect[]{effect}, null);
        ItemUsageResult result = new ItemUsageResult(item, player);
        item.usedBy(player, result);

        assertEquals(1, player.getBuffs().size());
        assertTrue(monster.getBuffs().isEmpty());
    }

}
