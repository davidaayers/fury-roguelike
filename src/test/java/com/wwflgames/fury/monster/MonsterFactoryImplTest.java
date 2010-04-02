package com.wwflgames.fury.monster;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class MonsterFactoryImplTest {

    @Before
    public void setUpTest() throws Exception {
    }

    @Test
    @Ignore
    public void testCreateMonsterWithTwoTemplatesChoosesRightOneBasedOnPoints() throws Exception {
        List<MonsterTemplate> templates = new ArrayList<MonsterTemplate>();

        MonsterTemplate t1 = new MonsterTemplate("1", "foo", 1, 3);
        MonsterTemplate t2 = new MonsterTemplate("2", "foo", 4, 6);
        templates.add(t1);
        templates.add(t2);
        MonsterFactoryImpl monsterFactory = new TestableMonsterFactoryImpl(templates);

        Monster m = monsterFactory.createMonster(1);
        assertEquals("1", m.name());
        Monster m2 = monsterFactory.createMonster(5);
        assertEquals("2", m2.name());

    }

    public class TestableMonsterFactoryImpl extends MonsterFactoryImpl {

        public TestableMonsterFactoryImpl(List<MonsterTemplate> templates) throws SlickException {
            super(null);
            installAllMonsters(templates);
        }

        @Override
        void parseXml() throws SlickException {
        }
    }
}
