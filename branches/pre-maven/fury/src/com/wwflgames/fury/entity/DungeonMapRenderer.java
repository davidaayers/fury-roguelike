package com.wwflgames.fury.entity;

import com.wwflgames.fury.gamestate.PlayerController;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Stairs;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.map.TileType;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class DungeonMapRenderer extends AbstractDungeonMapRenderer {

    private Image lock;

    public DungeonMapRenderer(String id, DungeonMap dungeonMap, PlayerController playerController) throws SlickException {
        super(id, dungeonMap, playerController);
        lock = new Image("lock.png");
    }

    @Override
    protected void doRender(int x, int y, Tile mapTile, Graphics graphics) {
        if (!owner.isVisible()) {
            return;
        }
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();

        Image drawImage = determineImageForTile(mapTile.getType());
        int tw = drawImage.getWidth();
        int th = drawImage.getHeight();
        float drawX = pos.x + (x * tw * scale);
        float drawY = pos.y + (y * th * scale);

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
