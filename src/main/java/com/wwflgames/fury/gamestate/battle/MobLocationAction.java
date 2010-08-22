package com.wwflgames.fury.gamestate.battle;

import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.slick.entity.Action;
import org.newdawn.slick.geom.Vector2f;

import static com.wwflgames.fury.Fury.*;

public class MobLocationAction extends Action {

    private Mob mob;
    private int mapOffsetX;
    private int mapOffsetY;
    private int screenOffsetX;
    private int screenOffsetY;

    public MobLocationAction(String id) {
        super(id);
    }

    public MobLocationAction setMob(Mob mob) {
        this.mob = mob;
        return this;
    }

    public MobLocationAction setMapOffset(int mapOffsetX, int mapOffsetY) {
        this.mapOffsetX = mapOffsetX;
        this.mapOffsetY = mapOffsetY;
        return this;
    }

    public MobLocationAction setScreenOffset(int screenOffsetX, int screenOffsetY) {
        this.screenOffsetX = screenOffsetX;
        this.screenOffsetY = screenOffsetY;
        return this;
    }

    @Override
    public void update(int delta) {
        // based on the mob's location, set the owning entity's position
        Tile mobTile = mob.getCurrentMapTile();

        int drawX = mobTile.getX() - mapOffsetX;
        int drawY = mobTile.getY() - mapOffsetY;

        float screenX = screenOffsetX + (drawX * TILE_WIDTH * owner.getScale()) + (4 * owner.getScale());
        float screenY = screenOffsetY + (drawY * TILE_HEIGHT * owner.getScale());

        owner.setPosition(new Vector2f(screenX, screenY));
    }
}
