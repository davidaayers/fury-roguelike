package com.wwflgames.fury.monster;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StatRangeTest {

    @Test
    public void testValueForPercent() throws Exception {
        StatRange r1 = new StatRange(10, 20);
        assertEquals(10, r1.valueForPercent(0d));
        assertEquals(15, r1.valueForPercent(.50d));
        assertEquals(13, r1.valueForPercent(.33d));
        assertEquals(16, r1.valueForPercent(.66d));
    }
}
