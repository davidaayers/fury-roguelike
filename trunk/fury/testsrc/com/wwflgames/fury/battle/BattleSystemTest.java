package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.util.Shuffler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class BattleSystemTest {

    private BattleSystem battleSystem;
    private ItemFactory itemFactory;

    @Before
    public void setUp() throws Exception {
        Shuffler.installShuffleProvider(new Shuffler.ShuffleProvider() {
            @Override
            public void shuffle(List list) {
            }
        });

        itemFactory = new ItemFactory();
    }

    @After
    public void afterTest() {
        Shuffler.resetShuffleProvider();
    }

    @Test
    public void testOneRoundBattleAgainstOneMobWithPlayerInitiative() {
        Battle b = createBattle(10, 10, 5, 1, true);
        battleSystem = new BattleSystem(b);

        doBattle(b);

        assertTrue(battleSystem.didPlayerWin());
        assertEquals(new Integer(10), b.getPlayer().getStatValue(Stat.HEALTH));
        assertEquals(new Integer(-5), b.getOriginalEnemies().get(0).getStatValue(Stat.HEALTH));
    }

    @Test
    public void testOneRoundBattleAgainstOneMobWithMobInitiative() {
        Battle b = createBattle(10, 10, 5, 1, false);
        battleSystem = new BattleSystem(b);

        doBattle(b);

        assertTrue(battleSystem.didPlayerWin());
        assertEquals(new Integer(9), b.getPlayer().getStatValue(Stat.HEALTH));
        assertEquals(new Integer(-5), b.getOriginalEnemies().get(0).getStatValue(Stat.HEALTH));
    }

    @Test
    public void testTwoRoundBattleAgainstOneMobWithPlayerInitiative() {
        Battle b = createBattle(10, 5, 10, 1, true);
        battleSystem = new BattleSystem(b);

        doBattle(b);

        assertTrue(battleSystem.didPlayerWin());
        assertEquals(new Integer(9), b.getPlayer().getStatValue(Stat.HEALTH));
        assertEquals(new Integer(0), b.getOriginalEnemies().get(0).getStatValue(Stat.HEALTH));
    }

    @Test
    public void testTwoRoundBattleAgainstOneMobWithMobInitiative() {
        Battle b = createBattle(10, 5, 10, 1, false);
        battleSystem = new BattleSystem(b);

        doBattle(b);

        assertTrue(battleSystem.didPlayerWin());
        assertEquals(new Integer(8), b.getPlayer().getStatValue(Stat.HEALTH));
        assertEquals(new Integer(0), b.getOriginalEnemies().get(0).getStatValue(Stat.HEALTH));
    }

    @Test
    public void testBattleRoundResult() {
        Battle b = createBattle(10, 5, 10, 1, false);
        battleSystem = new BattleSystem(b);
        battleSystem.startBattle();
        BattleRoundResult result = battleSystem.performBattleRound((Monster) b.getEnemies().get(0));

        System.out.println("========= replaying battle ==========");
//        List<ItemUsageResult> playerEffects = result.monsterItemEffectList();
//        printBattleEffectList(playerEffects);
//        List<ItemUsageResult> monsterEffects = result.playerItemEffectList();
//        printBattleEffectList(monsterEffects);
    }

    private void printBattleEffectList(List<ItemUsageResult> playerEffects) {
        for (ItemUsageResult bel : playerEffects) {
            System.out.println(bel.mob().name() + " was effected by " + bel.item().name());
            List<ItemEffectResult> beList = bel.get();
            for (ItemEffectResult be : beList) {
                System.out.println(" " + be.toString());
            }
        }
    }

    private Battle createBattle(int playerHealth, int playerDmg, int mobHealth, int mobDamage,
                                boolean playerInitiative) {
        Player player = newPlayer("Player", playerHealth);
        player.getDeck().addItem(newItem(playerDmg));
        Monster enemy = newMonster("Enemy 1", mobHealth);
        enemy.getDeck().addItem(newItem(mobDamage));
        List<Monster> mobs = new ArrayList<Monster>();
        mobs.add(enemy);

        Battle b = new Battle(player, mobs, playerInitiative);
        return b;
    }

    private Monster newMonster(String s, int mobHealth) {
        Monster mob = new Monster(s, "foo", 0);
        mob.setStatValue(Stat.HEALTH, mobHealth);
        mob.setDeck(new ItemDeck());
        return mob;
    }

    private Player newPlayer(String s, int mobHealth) {
        Player mob = new Player(s, null);
        mob.setStatValue(Stat.HEALTH, mobHealth);
        mob.setDeck(new ItemDeck());
        return mob;
    }

    private void doBattle(Battle battle) {
        battleSystem.startBattle();
        while (!battleSystem.isBattleOver()) {
            battleSystem.performBattleRound((Monster) battle.getEnemies().get(0));
        }
    }

    private Mob newMob(String name, int health) {
        Mob mob = new Mob(name);
        mob.setStatValue(Stat.HEALTH, health);
        mob.setDeck(new ItemDeck());
        return mob;
    }

    private Item newItem(final int damage) {
        return null;
    }

}
