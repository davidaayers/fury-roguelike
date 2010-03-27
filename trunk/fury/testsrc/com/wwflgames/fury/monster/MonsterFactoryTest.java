package com.wwflgames.fury.monster;

import com.wwflgames.fury.item.ItemFactory;
import org.junit.Before;
import org.junit.Test;

public class MonsterFactoryTest {
    private MonsterFactory monsterFactory;

    @Before
    public void setUp() throws Exception {
        monsterFactory = new MonsterFactory(new ItemFactory());
    }

    @Test
    public void testStuff() throws Exception {
        // do stuff
    }
}
