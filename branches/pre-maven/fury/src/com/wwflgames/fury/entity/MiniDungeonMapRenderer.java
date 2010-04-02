package com.wwflgames.fury.entity;

import com.wwflgames.fury.gamestate.PlayerController;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class MiniDungeonMapRenderer extends AbstractDungeonMapRenderer {
    public MiniDungeonMapRenderer(String id, DungeonMap dungeonMap, PlayerController playerController)
            throws SlickException {
        super(id, dungeonMap, playerController);
    }

    @Override
    public void render(Graphics gr) {
        for (int y = 0; y < dungeonMap.getHeight(); y++) {
            for (int x = 0; x < dungeonMap.getWidth(); x++) {
                Tile mapTile = dungeonMap.getTileAt(x, y);
                doRender(x, y, mapTile, gr);
            }
        }
    }


    @Override
    protected void doRender(int x, int y, Tile mapTile, Graphics graphics) {
        if (!owner.isVisible()) {
            return;
        }
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();

        float tw = 32;
        float th = 32;
        float drawX = pos.x + (x * tw * scale);
        float drawY = pos.y + (y * th * scale);

        Color oldColor = graphics.getColor();
        Color newColor;
        switch (mapTile.getType()) {
            case WALL:
                newColor = Color.gray;
                break;
            case FLOOR:
                newColor = Color.white;
                break;
            default:
                newColor = Color.black;
        }
        graphics.setColor(newColor);


        if (mapTile.hasPlayerSeen()) {
            graphics.fillRect(drawX, drawY, tw * scale, th * scale);
        }
        graphics.setColor(oldColor);

    }
}
