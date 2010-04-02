package com.wwflgames.fury.entity;

import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.mob.Mob;
import org.newdawn.slick.geom.Vector2f;

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

        float screenX = screenOffsetX + (drawX * 32 * owner.getScale()) + (4 * owner.getScale());
        float screenY = screenOffsetY + (drawY * 32 * owner.getScale());

        owner.setPosition(new Vector2f(screenX, screenY));
    }
}
