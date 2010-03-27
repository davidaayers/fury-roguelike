package com.wwflgames.fury.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.HashMap;
import java.util.Map;

public class SpriteSheetCache {

    private Map<String, SpriteSheet> spriteSheetMap = new HashMap<String, SpriteSheet>();

    public void loadSprite(String spriteSheetName) throws SlickException {
        // treat green as transparent
        SpriteSheet ss = new SpriteSheet(spriteSheetName, 24, 32, new Color(32, 156, 0));
        ss.setAlpha(1f);
        spriteSheetMap.put(spriteSheetName, ss);
    }

    public SpriteSheet getSpriteSheet(String spriteSheetName) {
        return spriteSheetMap.get(spriteSheetName);
    }

}
