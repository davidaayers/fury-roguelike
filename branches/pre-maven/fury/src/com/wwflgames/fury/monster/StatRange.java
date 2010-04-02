package com.wwflgames.fury.monster;

public class StatRange {
    int low;
    int high;

    public StatRange(int low, int high) {
        this.low = low;
        this.high = high;
    }

    // given a pct, find the matching value starting with low and
    // ending with high.
    // So, 0% should match low, and 100% should match high, and anything
    // in between should be some number between low and high
    public int valueForPercent(double pct) {
        int diff = high - low;
        return (int) (low + (diff * pct));
    }

}
