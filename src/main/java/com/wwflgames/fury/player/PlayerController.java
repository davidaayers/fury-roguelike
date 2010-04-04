package com.wwflgames.fury.player;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.util.BresenhamLine;
import com.wwflgames.fury.util.Log;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.Fury.TILE_HEIGHT;
import static com.wwflgames.fury.Fury.TILE_WIDTH;

public class PlayerController {

    private int offsetX;
    private int offsetY;
    private int eastEdgeX;
    private int westEdgeX;
    private int northEdgeY;
    private int southEdgeY;
    private Player player;
    private DungeonMap map;

    public PlayerController(Player player, DungeonMap map) {
        this.player = player;
        this.map = map;
        setInitialOffsets();
        updateCurrentFeatureSeen();
    }

    private void setInitialOffsets() {
        // we want to center the player on the screen, so set the offsets
        // appropriately
        Integer playerX = player.getMapX();
        Integer playerY = player.getMapY();
        offsetX = playerX - Fury.MAP_WIDTH/2;
        offsetY = playerY - Fury.MAP_HEIGHT/2;
        eastEdgeX = playerX + 9;
        westEdgeX = playerX - 9;
        southEdgeY = playerY + 6;
        northEdgeY = playerY - 7;
        printStuff();
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public Player getPlayer() {
        return player;
    }

    public Rectangle getPlayerRectangle() {
        return new Rectangle(getDrawX(),getDrawY(),TILE_WIDTH,TILE_HEIGHT);
    }

    public float getDrawX() {
        Tile currentMapTile = player.getCurrentMapTile();
        int mapX = currentMapTile.getX();
        mapX -= this.getOffsetX();
        float drawX = mapX * TILE_WIDTH;
        return drawX;
    }

    public float getDrawY() {
        Tile currentMapTile = player.getCurrentMapTile();
        int mapY = currentMapTile.getY();
        mapY -= this.getOffsetY();
        float drawY = mapY * TILE_HEIGHT;
        return drawY;
    }

    public void movePlayerTo(int x, int y) {
        Log.debug("Moving to " + x + "," + y);
        int oldX = player.getMapX();
        int oldY = player.getMapY();

        map.removeMob(player);
        map.addMob(player, x, y);

        int dx = x - oldX;
        int dy = y - oldY;

        // handle all of the offset cases
        // move EAST
        if (dx == 1) {
            if (x > eastEdgeX) {
                offsetX++;
                eastEdgeX++;
                westEdgeX++;
            }
        }
        // WEST
        if (dx == -1) {
            if (x < westEdgeX) {
                offsetX--;
                westEdgeX--;
                eastEdgeX--;
            }
        }
        // SOUTH
        if (dy == 1) {
            if (y > southEdgeY) {
                offsetY++;
                southEdgeY++;
                northEdgeY++;
            }
        }
        // NORTH
        if (dy == -1) {
            if (y < northEdgeY) {
                offsetY--;
                southEdgeY--;
                northEdgeY--;
            }
        }

        printStuff();
        updateCurrentFeatureSeen();
    }

    private void updateCurrentFeatureSeen() {
        // now, update the player visibility level
        map.resetVisibility();

        List<Tile> tiles = new ArrayList<Tile>();

        int deltaX = 7;
        int deltaY = 1;
        while (deltaX > 0) {
            int startX = player.getMapX() - deltaX + 1;
            int endX = player.getMapX() + deltaX;
            int startY = player.getMapY() - deltaY + 1;
            int endY = player.getMapY() + deltaY;

            for (int y = startY; y < endY; y++) {
                for (int x = startX; x < endX; x++) {
                    if (map.inBounds(x, y)) {
                        Tile tile = map.getTileAt(x, y);
                        tile.setPlayerVisibility(tile.getPlayerVisibility() + 1);
                        if (!tiles.contains(tile)) {
                            tiles.add(tile);
                        }
                    }
                }
            }
            deltaX--;
            if (startX < player.getMapX()) {
                deltaY++;
            } else {
                deltaY--;
            }
        }

        // finally add a LOS check for every tile in tiles
        int playerX = player.getMapX();
        int playerY = player.getMapY();
        for (Tile tile : tiles) {
            BresenhamLine line = new BresenhamLine();
            int tileX = tile.getX();
            int tileY = tile.getY();
            line.plot(playerX, playerY, tileX, tileY);
            while (line.next()) {
                int lineX = line.getX();
                int lineY = line.getY();
                Tile checkTile = map.getTileAt(lineX, lineY);
                // mark every tile on the line as "seen"
                checkTile.setHasPlayerSeen(true);
                if (!checkTile.isWalkable()) {
                    // we hit a non-walkable tile in between the player
                    // and the destination, mark the destination as not visible
                    tile.setPlayerVisibility(0);
                    break;
                }
            }
        }

        // mark the player's location as "seen", not sure yet why I have to do this
        player.getCurrentMapTile().setHasPlayerSeen(true);

    }

    private void printStuff() {
        Log.debug("offsetX=" + offsetX);
        Log.debug("offsetY=" + offsetY);
        Log.debug("westEdgeX=" + westEdgeX);
        Log.debug("eastEdgeX=" + eastEdgeX);
        Log.debug("northEdgeY=" + northEdgeY);
        Log.debug("southEdgeY=" + southEdgeY);
    }


}
