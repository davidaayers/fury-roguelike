package com.wwflgames.fury.util;

import java.util.Collections;
import java.util.List;

public class Shuffler {

    private static ShuffleProvider shuffleProvider = defaultShuffleProvider();

    private static ShuffleProvider defaultShuffleProvider() {
        return new ShuffleProvider() {
            public void shuffle(List list) {
                Collections.shuffle(list);
            }
        };
    }

    public static void installShuffleProvider(ShuffleProvider provider) {
        shuffleProvider = provider;
    }

    public static void resetShuffleProvider() {
        shuffleProvider = defaultShuffleProvider();
    }

    public static void shuffle(List list) {
        shuffleProvider.shuffle(list);
    }

    public interface ShuffleProvider {
        void shuffle(List list);
    }

}
