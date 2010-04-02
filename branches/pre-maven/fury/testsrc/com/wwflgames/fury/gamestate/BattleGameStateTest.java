package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.map.DifficultyLevel;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.FixedDungeonMapCreator;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class BattleGameStateTest {
    private BattleGameState battleGameState;

    @Before
    public void setUp() throws Exception {
        // don't need app state for anything yet in this test
        battleGameState = new BattleGameState(null, null, null);
    }

    @Test
    public void findMonstersActuallyFindsMonstersAndIgnoresPlayer() {
        // set up a test dungeonMap
        DungeonMap dungeonMap = new FixedDungeonMapCreator().createMap(DifficultyLevel.EASY, 1);
        Player p1 = new Player("hero", null);
        Monster m1 = new Monster("foo", "foo", 0);
        Monster m2 = new Monster("bar", "foo", 0);
        dungeonMap.addMob(m1, 1, 1);
        dungeonMap.addMob(m2, 3, 3);
        dungeonMap.addMob(p1, 2, 2);

        assertEquals(new Integer(2), p1.getMapX());
        assertEquals(new Integer(2), p1.getMapY());

        List<Monster> monsters = battleGameState.findMonsters(dungeonMap, p1.getMapX(), p1.getMapY());
        assertEquals(2, monsters.size());
    }
}
