package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.battle.BattleResult;
import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DamageApplierTest {
    private Player player;
    private Monster monster;
    private Card card;
    private BattleRound battleRound;

    @Before
    public void setUp() throws Exception {
        player = new Player("foo",null);
        monster = new Monster("bar",null,1);
        card = new Card("foocard",null,null,1);
        battleRound = new BattleRound(1);
    }

    @After
    public void cleanUp() throws Exception {

        System.out.println("Results for player:" );
        for (BattleResult result: battleRound.getResultsFor(player)) {
            System.out.println(result.getDesc());
        }

        player.resetBattleStats();
        monster.resetBattleStats();
    }

    @Test
    public void testApplyToWithMeleeDamageAppliesNoBonusWhenStrengthIsZero() throws Exception {
        DamageApplier da = new DamageApplier(DamageType.CRUSH,10);
        da.applyTo(card,player,monster,battleRound);
        assertEquals(new Integer(-10),monster.getStatValue(Stat.HEALTH));
    }

    @Test
    public void testApplyToWithMeleeDamageAppliesBonusWhenStrengthIsNotZero() throws Exception {
        player.setStatValue(Stat.STRENGTH,20);
        player.prepareForBattle();
        DamageApplier da = new DamageApplier(DamageType.CRUSH,10);
        da.applyTo(card,player,monster,battleRound);
        assertEquals(new Integer(-12),monster.getStatValue(Stat.HEALTH));
    }

    @Test
    public void testApplyToWithSlashDamageAppliesTwoBonusesWhenStrengthAndDexAreNotZero() throws Exception {
        player.setStatValue(Stat.STRENGTH,20);
        player.setStatValue(Stat.DEXTERITY,20);
        player.prepareForBattle();
        DamageApplier da = new DamageApplier(DamageType.SLASH,10);
        da.applyTo(card,player,monster,battleRound);
        assertEquals(new Integer(-14),monster.getStatValue(Stat.HEALTH));
    }

    @Test
    public void testCrushMitigationThatFullyAbsorbsDamage() throws Exception {
        monster.setStatValue(Stat.ARMOR,20);
        monster.prepareForBattle();
        DamageApplier da = new DamageApplier(DamageType.CRUSH,10);
        da.applyTo(card,player,monster,battleRound);
        assertEquals(new Integer(0),monster.getStatValue(Stat.HEALTH));
        assertEquals(new Integer(10),monster.getBattleStatValue(Stat.ARMOR));
    }

    @Test
    public void testCrushMitigationThatParticallyAbsorbsDamage() throws Exception {
        monster.setStatValue(Stat.ARMOR,5);
        monster.prepareForBattle();
        DamageApplier da = new DamageApplier(DamageType.CRUSH,10);
        da.applyTo(card,player,monster,battleRound);
        assertEquals(new Integer(-5),monster.getStatValue(Stat.HEALTH));
        assertEquals(new Integer(0),monster.getBattleStatValue(Stat.ARMOR));
    }



}
