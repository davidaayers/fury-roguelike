package com.wwflgames.fury.util;

import com.wwflgames.fury.map.DungeonMap;

public class AsciiMapPrinter {

    public static void printMap(DungeonMap map) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                System.out.print(map.getTileAt(x, y).getType().getAscii());
            }
            System.out.println("");
        }
    }

}
