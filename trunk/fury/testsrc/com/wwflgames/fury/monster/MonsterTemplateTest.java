package com.wwflgames.fury.monster;

import com.wwflgames.fury.mob.Stat;
import org.junit.Test;

import static junit.framework.Assert.*;

public class MonsterTemplateTest {
    @Test
    public void testRelativePctIncrease() throws Exception {
        MonsterTemplate t1 = new MonsterTemplate("foo", "bar", 1, 3);
        assertEquals(0.0d, t1.relativePctIncrease(1));
        assertEquals(0.5d, t1.relativePctIncrease(2));
        assertEquals(1d, t1.relativePctIncrease(3));

        MonsterTemplate t2 = new MonsterTemplate("foo", "bar", 1, 4);
        assertEquals(0.0d, t2.relativePctIncrease(1));
        assertEquals(0.3333333333333333d, t2.relativePctIncrease(2));
        assertEquals(0.6666666666666666d, t2.relativePctIncrease(3));
        assertEquals(1d, t2.relativePctIncrease(4));
    }

    @Test
    public void testCreateForPoints() throws Exception {
        MonsterTemplate t = new MonsterTemplate("foo", "bar", 1, 3);
        StatRange testRange = new StatRange(10, 20);
        t.setStatRange(Stat.HEALTH, testRange);

        Monster m = t.createForLevel(new MonsterLevel(1, false));
        assertEquals(new Integer(10), m.getStatValue(Stat.HEALTH));
        Monster m2 = t.createForLevel(new MonsterLevel(2, false));
        assertEquals(new Integer(15), m2.getStatValue(Stat.HEALTH));
        Monster m3 = t.createForLevel(new MonsterLevel(3, false));
        assertEquals(new Integer(20), m3.getStatValue(Stat.HEALTH));
    }

    @Test
    public void testCreateNameWithOnlyPre() throws Exception {
        MonsterTemplate t = new MonsterTemplate("foo", "bar", 1, 3);
        t.addNameModifier("pre", new MonsterLevel(1, false), "strong");
        Monster m = t.createForLevel(new MonsterLevel(1, false));
        assertEquals("strong foo", m.name());
    }

    @Test
    public void testCreateNameWithOnlyPost() throws Exception {
        MonsterTemplate t = new MonsterTemplate("foo", "bar", 1, 3);
        t.addNameModifier("post", new MonsterLevel(1, false), "hero");
        Monster m = t.createForLevel(new MonsterLevel(1, false));
        assertEquals("foo hero", m.name());
    }

    @Test
    public void testCreateNameWithPreAndPost() throws Exception {
        MonsterTemplate t = new MonsterTemplate("foo", "bar", 1, 3);
        t.addNameModifier("pre", new MonsterLevel(1, false), "strong");
        t.addNameModifier("post", new MonsterLevel(1, false), "hero");
        Monster m = t.createForLevel(new MonsterLevel(1, false));
        assertEquals("strong foo hero", m.name());
    }
}
