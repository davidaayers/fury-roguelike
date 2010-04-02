package com.wwflgames.fury.util;

import org.junit.Test;

import java.util.Random;

import static junit.framework.Assert.assertEquals;

public class RandTest {

    @Test
    public void testBetweenWithNegativeNumbersWorks() throws Exception {
        Rand.installRandom(new Random(1));
        // with a seed of 1, these should always return in this order
        assertEquals(-5,Rand.between(-10,10));
        assertEquals(-2,Rand.between(-10,10));
        assertEquals(-3,Rand.between(-10,10));
        assertEquals(3,Rand.between(-10,10));
        assertEquals(4,Rand.between(-10,10));
    }

}
