package com.wwflgames.fury.util;

import java.util.Random;

public class Rand {

    private static Random installedRandom = defaultRandom();


    private static Random defaultRandom() {
        return new Random();
    }

    public static void installRandom(Random random) {
        installedRandom = random;
    }

    public static Random get() {
        return installedRandom;
    }

    public static int between(int min, int max) {
        int delta = max - min;
        if (delta <= 0) {
            return min;
        }
        return min + get().nextInt(delta);
    }

}
