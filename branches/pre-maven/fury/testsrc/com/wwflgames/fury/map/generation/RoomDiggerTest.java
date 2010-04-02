package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.util.AsciiMapPrinter;
import com.wwflgames.fury.map.Direction;
import com.wwflgames.fury.map.DungeonMap;
import org.junit.Test;

public class RoomDiggerTest {

    @Test
    public void testDigRoom() {

//        Rand.installRandom(new Random(1) {
//            @Override
//            public int nextInt(int n) {
//                return 2;
//            }
//        });

        DungeonMap map = new DungeonMap(20, 20);
        //RoomDigger roomDigger = new RoomDigger(5, 5, 8, 8);
        SquareRoomDigger roomDigger = new SquareRoomDigger(5,8);

        try {
            roomDigger.dig(map, new JoinPoint(10, 10, Direction.N));
        } catch (DigException e) {
            e.printStackTrace();
        }
        //AsciiMapPrinter.printMap(map);
//        RoomDigger roomDigger2 = new RoomDigger(5,5);
//        roomDigger2.dig(map,new JoinPoint(10,6, Direction.N));
        AsciiMapPrinter.printMap(map);


    }

}
