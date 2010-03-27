package com.wwflgames.fury.entity;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemImpl;
import com.wwflgames.fury.item.effect.ItemEffect;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;

public class ItemRenderer extends CardRenderer {

    private Item item;
    private UnicodeFont font;

    public ItemRenderer(Item item, UnicodeFont font) {
        super(item.name());
        this.item = item;
        this.font = font;
    }

    @Override
    protected void maybeRenderItemText(Graphics g) {

        // draw item name;
        int y = 4;
        drawString(item.name(), y, Color.white);

        ItemImpl itemImpl = (ItemImpl) item;
        // draw effects against
        if (itemImpl.getUsedAgainstEffects() != null) {
            for (ItemEffect effect : itemImpl.getUsedAgainstEffects()) {
                y += 14;
                drawString(effect.getDesc(), y, Color.red);
            }
        }
        // draw used by effects
        if (itemImpl.getUsedByEffects() != null) {
            for (ItemEffect effect : itemImpl.getUsedByEffects()) {
                y += 14;
                drawString(effect.getDesc(), y, Color.green);
            }
        }
    }

    private void drawString(String text, int y, Color fontColor) {
        Vector2f pos = owner.getPosition();
        int width = 32 * 4;
        int strWidth = font.getWidth(text);
        font.drawString(pos.x + (width / 2) - (strWidth / 2), pos.y + y, text, fontColor);

    }

}
