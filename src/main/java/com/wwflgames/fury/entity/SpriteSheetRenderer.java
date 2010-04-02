package com.wwflgames.fury.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class SpriteSheetRenderer extends Renderer {

    private SpriteSheet spriteSheet;
    private int spriteCol;
    private int spriteRow;

    public SpriteSheetRenderer(String id, SpriteSheet spriteSheet) {
        super(id);
        this.spriteSheet = spriteSheet;
    }

    public SpriteSheetRenderer useSprite(int spriteCol, int spriteRow) {
        this.spriteCol = spriteCol;
        this.spriteRow = spriteRow;
        return this;
    }

    @Override
    public void render(Graphics gr) {
        if (owner.isVisible()) {
            Vector2f pos = owner.getPosition();
            float scale = owner.getScale();
            doDrawSpriteSheet(pos, scale);
        }
    }

    protected void doDrawSpriteSheet(Vector2f pos, float scale) {
        current().draw(pos.x, pos.y, scale);
    }

    @Override
    public void update(int delta) {
        current().rotate(owner.getRotation() - current().getRotation());

    }

    protected Image current() {
        return spriteSheet.getSprite(spriteCol, spriteRow);
    }
}
