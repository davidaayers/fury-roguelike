package com.wwflgames.fury.entity;

import com.wwflgames.fury.gamestate.PlayerController;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

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
        int tw = drawImage.getWidth();
        int th = drawImage.getHeight();
        float drawX = pos.x + (x * tw * scale);
        float drawY = pos.y + (y * th * scale);

        if (mapTile.hasPlayerSeen()) {
            drawImage.draw(drawX, drawY, scale);
        }

        //debug map visibility
        //graphics.drawString(""+mapTile.getPlayerVisibility(),drawX+7,drawY+7);
    }
}
