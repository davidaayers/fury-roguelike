package com.wwflgames.fury.map;

import com.wwflgames.fury.map.generation.Feature;
import com.wwflgames.fury.util.AsciiMapPrinter;
import com.wwflgames.fury.util.Log;

public class NewDungeonMapCreator implements DungeonMapCreator {
    @Override
    public DungeonMap createMap(DifficultyLevel difficulty, int level) {

        DungeonMap dungeonMap = new DungeonMap(21, 21);

        // create a 5x5 setup of features
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                Feature f = buildFeature(dungeonMap, x, y);
                dungeonMap.addFeature(f);

            }
        }

        AsciiMapPrinter.printMap(dungeonMap);

        return dungeonMap;
    }

    private Feature buildFeature(DungeonMap map, int x, int y) {

        Feature f = new Feature();

        // the x,y coord on the map of this feature
        int fX = x * 4;
        int fY = y * 4;

        Log.debug("fx = " + fX);
        Log.debug("fy = " + fY);

        // each room is 5x5, with doors in the middle of each wall:
        //
        // ## ##
        // #   #
        //
        // #   #
        // ## ##

        // draw the top, bottom, left and right, and put the doors in
        for (int i = 0; i < 5; i++) {

            int topX = fX + i;
            int bottomY = fY + 4;
            int leftY = fY + i;
            int rightX = fX + 4;

            System.out.println("rightX = " + rightX);
            System.out.println("leftY = " + leftY);
            System.out.println("bottomY = " + bottomY);
            System.out.println("topX = " + topX);

            if (i == 2) {

                Tile topDoor = map.getTileAt(topX, fY);
                if ( fY != 0 ) {
                    topDoor.setType(TileType.FLOOR);
                    f.addFloorTile(topDoor);
                } else {
                    topDoor.setType(TileType.WALL);
                    f.addWallTile(topDoor);
                }

                Tile bottomDoor = map.getTileAt(topX, bottomY);
                if ( bottomY != 20 ) {
                    bottomDoor.setType(TileType.FLOOR);
                    f.addFloorTile(bottomDoor);
                } else {
                    bottomDoor.setType(TileType.WALL);
                    f.addWallTile(bottomDoor);
                }

                Tile leftDoor = map.getTileAt(fX, leftY);
                if ( fX != 0 ) {
                    leftDoor.setType(TileType.FLOOR);
                    f.addFloorTile(leftDoor);
                } else {
                    leftDoor.setType(TileType.WALL);
                    f.addWallTile(leftDoor);
                }

                Tile rightDoor = map.getTileAt(rightX, leftY);
                if ( rightX != 20 ) {
                    rightDoor.setType(TileType.FLOOR);
                    f.addFloorTile(rightDoor);
                } else {
                    rightDoor.setType(TileType.WALL);
                    f.addWallTile(rightDoor);
                }
            } else {
                Tile top = map.getTileAt(topX, fY);
                top.setType(TileType.WALL);
                f.addWallTile(top);

                Tile bottom = map.getTileAt(topX, bottomY);
                bottom.setType(TileType.WALL);
                f.addWallTile(bottom);

                Tile left = map.getTileAt(fX, leftY);
                left.setType(TileType.WALL);
                f.addWallTile(left);

                Tile right = map.getTileAt(rightX, leftY);
                right.setType(TileType.WALL);
                f.addWallTile(right);
            }
        }

        // fill in the floor
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                Tile floor = map.getTileAt(fX + j, fY + i);
                floor.setType(TileType.FLOOR);
                f.addFloorTile(floor);
            }
        }




        return f;
    }

    public static void main(String[] args) {
        NewDungeonMapCreator m = new NewDungeonMapCreator();
        m.createMap(null,1);
    }
}
