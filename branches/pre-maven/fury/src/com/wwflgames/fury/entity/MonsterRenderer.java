package com.wwflgames.fury.entity;

import com.wwflgames.fury.monster.Monster;
import org.newdawn.slick.Color;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class MonsterRenderer extends SpriteSheetRenderer {
    private Monster monster;

    public MonsterRenderer(String id, SpriteSheet spriteSheet, Monster monster) {
        super(id, spriteSheet);
        this.monster = monster;
    }

    protected void doDrawSpriteSheet(Vector2f pos, float scale) {
        if (monster.isBoss()) {
            current().draw(pos.x, pos.y, scale, Color.red);
        } else {
            current().draw(pos.x, pos.y, scale);
        }
    }


}
