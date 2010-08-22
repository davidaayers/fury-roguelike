package com.wwflgames.fury.entity;

import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.map.TileType;
import com.wwflgames.slick.entity.Renderer;
import org.newdawn.slick.*;

import static com.wwflgames.fury.Fury.*;

public abstract class MapRenderer extends Renderer {

    protected SpriteSheet spriteSheet;
    protected DungeonMap dungeonMap;
    private Image empty;
    protected Image lock;
    

    public MapRenderer(String id, DungeonMap dungeonMap) throws SlickException {
        super(id);
        this.spriteSheet = new SpriteSheet("dg_dungeon32.gif", TILE_WIDTH, TILE_HEIGHT);
        this.dungeonMap = dungeonMap;

        // create an empty, black square for rendering "EMPTY" tiles
        empty = new Image(TILE_WIDTH, TILE_HEIGHT);
        Graphics g = empty.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
        g.flush();

        lock = new Image("lock.png");
    }

    protected Image determineImageForTile(TileType tileType) {
        Image floorImage = spriteSheet.getSprite(0, 8);
        Image wallImage = spriteSheet.getSprite(0, 0);
        Image stairImage = spriteSheet.getSprite(0,3);
        Image drawImage = floorImage;
        if (tileType == TileType.WALL) {
            drawImage = wallImage;
        } else if (tileType == TileType.EMPTY) {
            drawImage = empty;
        } else if ( tileType == TileType.STAIR ) {
            drawImage = stairImage;
        }
        return drawImage;
    }

}
