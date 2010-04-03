package com.wwflgames.fury.entity;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.gamestate.PlayerController;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Stairs;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.map.TileType;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import static com.wwflgames.fury.Fury.*;

public class DungeonMapRenderer extends AbstractDungeonMapRenderer {

    public DungeonMapRenderer(String id, DungeonMap dungeonMap, PlayerController playerController) throws SlickException {
        super(id, dungeonMap, playerController);
    }

    @Override
    protected void doRender(int x, int y, Tile mapTile, Graphics graphics) {
        if (!owner.isVisible()) {
            return;
        }
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();

        Image drawImage = determineImageForTile(mapTile.getType());
        float drawX = pos.x + (x * TILE_WIDTH * scale);
        float drawY = pos.y + (y * TILE_HEIGHT * scale);

        if (mapTile.hasPlayerSeen()) {
            drawImage.draw(drawX, drawY, scale);

            // if the map tile is stairs, and they are locked, also draw the lock image on top
            if (mapTile.getType() == TileType.STAIR) {
                Stairs s = mapTile.getStairs();
                if (s.areLocked()) {
                    lock.draw(drawX, drawY, scale);
                }
            }
        }
    }
}
