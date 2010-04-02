package com.wwflgames.fury.map;

import com.wwflgames.fury.util.AsciiMapPrinter;
import org.junit.Test;

public class DungeonMapCreatorImplTest {

    @Test
    public void testCreateMap() throws Exception {
        DungeonMapCreatorImpl mapCreator = new DungeonMapCreatorImpl(null);
        DungeonMap map = mapCreator.createMap(DifficultyLevel.EASY,5);
        AsciiMapPrinter.printMap(map);
    }

}
