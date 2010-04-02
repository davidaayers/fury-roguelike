package com.wwflgames.fury.map.generation;

import com.wwflgames.fury.map.Direction;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.map.TileType;
import com.wwflgames.fury.util.Log;
import com.wwflgames.fury.util.Rand;

public abstract class AbstractDigger implements Digger {

    protected Feature maybeDigFeature(DungeonMap map, int startX, int endX, int startY, int endY) throws DigException {
        try {
            // make a copy of the map
            DungeonMap clone = map.duplicate();
            tryToDigFeature(clone, startX, endX, startY, endY);
            // now really dig it
            Feature feature = tryToDigFeature(map, startX, endX, startY, endY);
            return feature;
        } catch (DigException de) {
            throw de;
        }
    }

    private Feature tryToDigFeature(DungeonMap map, int startX, int endX, int startY, int endY) throws DigException {
        Feature newFeature = new Feature();
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                // see what's already there on the map
                // what are we drawing?
                TileType drawTile = TileType.FLOOR;
                if (y == startY || y == endY - 1 || x == startX || x == endX - 1) {
                    drawTile = TileType.WALL;
                }

                if (!map.inBounds(x, y)) {
                    throw new DigException();
                }

                Tile tile = map.getTileAt(x, y);
                TileType existing = tile.getType();

                boolean okToDraw = false;

                if (drawTile == TileType.WALL) {
                    // walls can be drawn on empty squares, or on top of existing walls
                    okToDraw = existing == TileType.EMPTY || existing == TileType.WALL || existing == TileType.JOIN;
                } else {
                    okToDraw = existing == TileType.EMPTY;
                }

                if (okToDraw) {
                    tile.setType(drawTile);
                    if (drawTile == TileType.FLOOR) {
                        newFeature.addFloorTile(tile);
                    } else if (drawTile == TileType.WALL) {
                        newFeature.addWallTile(tile);
                    }
                } else {
                    throw new DigException();
                }
            }
        }

        return newFeature;
    }

    protected void addJoinPoints(int howMany, Feature feature, DungeonMap map) {
        Tile[] wallTiles = feature.getWallTiles();
        int c = 0;
        int trys = 0;
        while (c < howMany && trys < 20) {
            int idx = Rand.get().nextInt(wallTiles.length);
            Tile t = wallTiles[idx];
            if (t.getType() != TileType.JOIN) {
                if (isCorner(t, map)) {
                    Log.debug("Found a corner, not using it");
                    continue;
                }
                Direction dir = findDirection(t, map);
                if (dir != null) {
                    JoinPoint jp = new JoinPoint(t.getX(), t.getY(), dir);
                    t.setType(TileType.JOIN);
                    feature.addJoinPoint(jp);
                    c++;
                }
            }
            trys++;
        }
    }

    private boolean isCorner(Tile tile, DungeonMap map) {
        int emptyCnt = 0;
        for (Direction cardinal : Direction.CARDINALS) {
            int checkX = tile.getX() + cardinal.getDx();
            int checkY = tile.getY() + cardinal.getDy();
            if (map.inBounds(checkX, checkY)) {
                if (map.getTileAt(checkX, checkY).getType() == TileType.EMPTY) {
                    emptyCnt++;
                }
            }
        }
        return emptyCnt > 1;
    }

    private Direction findDirection(Tile tile, DungeonMap map) {

        Log.debug("Finding direction for tile " + tile);

        // look in all of the cardinal directions. As soon as we fine one with a BLANK tile,
        // that's the direction our Join Point faces
        for (Direction cardinal : Direction.CARDINALS) {
            Log.debug("Looking in direction " + cardinal);
            int checkX = tile.getX() + cardinal.getDx();
            int checkY = tile.getY() + cardinal.getDy();
            Log.debug("checkx = " + checkX + " checky = " + checkY);
            if (map.inBounds(checkX, checkY)) {
                Tile checkTile = map.getTileAt(checkX, checkY);
                Log.debug("In Bounds, checkTile = " + checkTile);
                if (checkTile.getType() == TileType.EMPTY) {
                    Log.debug("Found an empty tile, this join point must face " + cardinal);
                    return cardinal;
                }
            }
        }
        // may happen on edge of map
        return null;
    }

}
