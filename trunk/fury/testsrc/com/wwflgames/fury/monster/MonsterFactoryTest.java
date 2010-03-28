package com.wwflgames.fury.monster;

import com.wwflgames.fury.item.ItemFactoryImpl;
import org.junit.Before;
import org.junit.Test;

public class MonsterFactoryTest {
    private MonsterFactory monsterFactory;

    @Before
    public void setUp() throws Exception {
        monsterFactory = new MonsterFactoryImpl(new ItemFactoryImpl());
    }

    @Test
    public void testStuff() throws Exception {
        // do stuff
    }
}
