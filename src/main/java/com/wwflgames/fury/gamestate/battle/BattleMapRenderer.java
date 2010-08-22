package com.wwflgames.fury.gamestate.battle;

import com.wwflgames.fury.gamestate.common.MapRenderer;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.map.TileType;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class BattleMapRenderer extends MapRenderer {

    private int renderX;
    private int renderY;

    public BattleMapRenderer(String id, DungeonMap dungeonMap, int renderX, int renderY)
            throws SlickException {
        super(id, dungeonMap);
        this.renderX = renderX;
        this.renderY = renderY;
    }

    @Override
    public void render(Graphics gr) {
        // starting at renderX, render Y, we're going to render 9 squares of the dungeonMap
        // if any of the squares would be out of bounds, don't draw them
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int mapX = renderX + x;
                int mapY = renderY + y;
                if (dungeonMap.inBounds(mapX, mapY)) {
                    Tile tile = dungeonMap.getTileAt(mapX, mapY);
                    TileType tileType = tile.getType();
                    Image drawImage = determineImageForTile(tileType);
                    int tw = drawImage.getWidth();
                    int th = drawImage.getHeight();
                    float posX = pos.x + (x * tw * scale);
                    float posY = pos.y + (y * th * scale);
                    drawImage.draw(posX, posY, scale);
                    if ( tileType == TileType.STAIR && tile.getStairs().areLocked() ) {
                        lock.draw(posX, posY, scale);
                    }
                }
            }
        }
    }

    @Override
    public void update(int delta) {
        // nothing to do here
    }
}
