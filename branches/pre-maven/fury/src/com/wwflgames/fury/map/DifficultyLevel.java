package com.wwflgames.fury.map;

public enum DifficultyLevel {
    EASY(5, 50, 80),
    NORMAL(10, 60, 80),
    HARD(15, 80, 100);

    private int numLevelsDeep;
    private int levelSizeMin;
    private int levelSizeMax;

    DifficultyLevel(int numLevelsDeep, int levelSizeMin, int levelSizeMax) {
        this.numLevelsDeep = numLevelsDeep;
        this.levelSizeMin = levelSizeMin;
        this.levelSizeMax = levelSizeMax;
    }

    public int getNumLevelsDeep() {
        return numLevelsDeep;
    }

    public int getLevelSizeMin() {
        return levelSizeMin;
    }

    public int getLevelSizeMax() {
        return levelSizeMax;
    }
}
