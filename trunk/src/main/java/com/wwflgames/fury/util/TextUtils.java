package com.wwflgames.fury.util;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static void centerText(GameContainer container, Graphics g, String text, int y) {
        centerText(0,container.getWidth(),g,text,y);
    }

    public static void centerText(int offsetX, int width, Graphics g, String text, int y ) {
        int x = offsetX + (width - g.getFont().getWidth(text)) /2;
        g.drawString(text, x, y);
    }

    public static List<String> maybeSplitString(String effectStr, int maxWidth, Font font) {

        String[] parts = effectStr.split(" ");
        List<String> splitString = new ArrayList<String>();
        int width = 0;
        String current = "";
        for (String part : parts) {
            width += font.getWidth(part + " ");
            if (width < maxWidth) {
                current = current + part + " ";
            } else {
                splitString.add(current);
                width = 0;
                current = part + " ";
            }
        }
        splitString.add(current);

        return splitString;
    }


}
