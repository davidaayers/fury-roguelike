package com.wwflgames.slick.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class ImageRenderer extends Renderer {

    private Image image;

    public ImageRenderer(String id, Image image) {
        super(id);
        this.image = image;
    }

    @Override
    public void render(Graphics gr) {
        Vector2f pos = owner.getPosition();
        float scale = owner.getScale();
        int width = image.getWidth();
        int height = image.getHeight();
        Color filterColor = (Color) owner.getProperty("filterColor");

        if (filterColor == null) {
            image.draw(pos.x, pos.y, scale);
        } else {
            image.draw(pos.x, pos.y, width * scale, height * scale, filterColor);
        }

    }

    @Override
    public void update(int delta) {
        image.rotate(owner.getRotation() - image.getRotation());
    }
}
