package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.item.ItemFactory;
import com.wwflgames.fury.item.ItemFactoryImpl;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.monster.MonsterFactory;
import com.wwflgames.fury.monster.MonsterFactoryImpl;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.player.Profession;
import com.wwflgames.fury.player.ProfessionFactoryImpl;
import com.wwflgames.fury.util.Shuffler;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class BattleSystemTest {

    private BattleSystem battleSystem;
    private ItemFactory itemFactory;
    private ProfessionFactoryImpl professionFactory;
    private MonsterFactory monsterFactory;

    @Before
    public void setUp() throws Exception {
        Shuffler.installShuffleProvider(new Shuffler.ShuffleProvider() {
            @Override
            public void shuffle(List list) {
            }
        });

        itemFactory = new ItemFactoryImpl();
        professionFactory = new ProfessionFactoryImpl(itemFactory);
        monsterFactory = new MonsterFactoryImpl(itemFactory);
    }

    @After
    public void afterTest() {
        Shuffler.resetShuffleProvider();
    }

    @Test
    public void testOneRoundBattleAgainstOneMobWithPlayerInitiative() {

        // create the player
        Profession p = professionFactory.getProfessionByName("Warrior");

        Player player = new Player("Bob",p);
        ItemDeck playerDeck = new ItemDeck();
        playerDeck.addItem(itemFactory.getItemByName("Crushing Blow"));
        player.installDeck(0,playerDeck);
        player.setDefaultDeck(0);
        player.setStatValue(Stat.HEALTH,50);

        Monster monster = new Monster("foo","sprites",1);
        ItemDeck monsterDeck = new ItemDeck();
        monsterDeck.addItem(itemFactory.getItemByName("Crushing Blow"));
        monster.setDeck(monsterDeck);
        List<Monster> monsters = new ArrayList<Monster>();
        monsters.add(monster);
        monster.setStatValue(Stat.HEALTH,10);


        Battle b = new Battle(player,monsters,true);
        battleSystem = new BattleSystem(b);

        doBattle(b);

        assertTrue("Player lost when they should have won!",battleSystem.didPlayerWin());
        assertEquals(new Integer(45), b.getPlayer().getStatValue(Stat.HEALTH));
        assertEquals(new Integer(-4), b.getOriginalEnemies().get(0).getStatValue(Stat.HEALTH));
    }



    private void doBattle(Battle battle) {
        battleSystem.startBattle();
        while (!battleSystem.isBattleOver()) {
            battleSystem.performBattleRound((Monster) battle.getEnemies().get(0));
        }
    }
}
