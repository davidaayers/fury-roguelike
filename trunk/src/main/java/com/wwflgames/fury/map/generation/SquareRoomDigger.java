package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.util.Rand;

import java.awt.*;

public class SquareRoomDigger extends RoomDigger {
    private int minSize;
    private int maxSize;

    public SquareRoomDigger(int minSize, int maxSize) {
        super(minSize, maxSize, minSize, maxSize);
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    protected Dimension determineDimension() {
        int size = Rand.between(minSize, maxSize);
        return new Dimension(size, size);
    }
}
