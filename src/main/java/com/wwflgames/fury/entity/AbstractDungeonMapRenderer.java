package com.wwflgames.fury.entity;

import com.wwflgames.fury.player.PlayerController;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.Tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class AbstractDungeonMapRenderer extends MapRenderer {
    protected PlayerController playerController;

    public AbstractDungeonMapRenderer(String id, DungeonMap dungeonMap, PlayerController playerController) throws SlickException {
        super(id, dungeonMap);
        this.playerController = playerController;
    }

    @Override
    public void render(Graphics gr) {
        for (int y = 0; y < dungeonMap.getHeight(); y++) {
            for (int x = 0; x < dungeonMap.getWidth(); x++) {
                int mapx = x + playerController.getOffsetX();
                int mapy = y + playerController.getOffsetY();
                if (mapx > dungeonMap.getWidth() - 1) {
                    mapx = dungeonMap.getWidth() - 1;
                }
                if (mapx < 0) {
                    mapx = 0;
                }
                if (mapy > dungeonMap.getHeight() - 1) {
                    mapy = dungeonMap.getHeight() - 1;
                }
                if (mapy < 0) {
                    mapy = 0;
                }
                Tile mapTile = dungeonMap.getTileAt(mapx, mapy);
                doRender(x, y, mapTile, gr);
            }
        }
    }

    protected abstract void doRender(int x, int y, Tile mapTile, Graphics graphics);

    @Override
    public void update(int delta) {
    }
}
