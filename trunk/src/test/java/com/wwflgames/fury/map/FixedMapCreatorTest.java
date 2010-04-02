package com.wwflgames.fury.map;

import org.junit.Before;
import org.junit.Test;

public class FixedMapCreatorTest {
    private FixedDungeonMapCreator mapCreator;

    @Before
    public void setUp() throws Exception {
        mapCreator = new FixedDungeonMapCreator();
    }

    @Test
    public void testCreateMap() {
        DungeonMap m = mapCreator.createMap(DifficultyLevel.EASY,1);

        for ( int y = 0 ; y < 5 ; y ++ ) {
            for ( int x = 0 ; x < 5 ; x ++ ) {
                System.out.print(m.getTileAt(x,y).getType().getAscii());
            }
            System.out.println("");
        }
    }
}
