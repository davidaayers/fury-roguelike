package com.wwflgames.fury.monster;

import com.wwflgames.fury.mob.Stat;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

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

        Monster m = t.createForPoints(1);
        assertEquals(new Integer(10), m.getStatValue(Stat.HEALTH));
        Monster m2 = t.createForPoints(2);
        assertEquals(new Integer(15), m2.getStatValue(Stat.HEALTH));
        Monster m3 = t.createForPoints(3);
        assertEquals(new Integer(20), m3.getStatValue(Stat.HEALTH));
    }
}
