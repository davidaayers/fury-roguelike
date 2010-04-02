package com.wwflgames.fury.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class TextUtil {

    public static void centerText(GameContainer container, Graphics g, String text, int y) {
        int x = (container.getWidth() - g.getFont().getWidth(text)) / 2;
        g.drawString(text, x, y);
    }

}
