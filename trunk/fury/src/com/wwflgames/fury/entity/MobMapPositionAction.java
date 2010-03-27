package com.wwflgames.fury.entity;

import com.wwflgames.fury.gamestate.PlayerController;
import com.wwflgames.fury.map.Tile;
import com.wwflgames.fury.mob.Mob;
import org.newdawn.slick.geom.Vector2f;

public class MobMapPositionAction extends Action {

    private Mob mob;
    private PlayerController playerController;

    public MobMapPositionAction(String id, Mob mob, PlayerController playerController) {
        super(id);
        this.mob = mob;
        this.playerController = playerController;
    }

    @Override
    public void update(int delta) {
        // get the mob's current position on the dungeonMap, and update the entity
        // based on it
        Tile currentMapTile = mob.getCurrentMapTile();
        int mapX = currentMapTile.getX();
        int mapY = currentMapTile.getY();

        // adjust mapX and mapY based on the offset from the playerController
        mapX -= playerController.getOffsetX();
        mapY -= playerController.getOffsetY();


        float drawX = mapX * 32 + 4;
        float drawY = mapY * 32;
        owner.setPosition(new Vector2f(drawX, drawY));

        //boolean visible = currentMapTile.getPlayerVisibility() != Tile.NOT_VISIBLE && currentMapTile.hasPlayerSeen();
        owner.setVisible(currentMapTile.isVisible());
    }
}
