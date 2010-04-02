package com.wwflgames.fury.ui;

import com.wwflgames.fury.item.Item;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ItemRenderer extends AbstractRenderer {

    private Item item;

    public ItemRenderer(Item item) {
        this.item = item;
    }

    @Override
    public void render(Graphics g) throws SlickException {
        g.setColor(Color.green);
        g.drawRoundRect(0, 0, 96, 96, 25);
    }

    @Override
    public void update(int delta) throws SlickException {

    }
}
