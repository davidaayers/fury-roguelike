package com.wwflgames.fury.entity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class SpriteSheetFactory {

    private Map<String, SpriteSheet> spriteSheetMap = new HashMap<String, SpriteSheet>();

    @Inject
    public SpriteSheetFactory(Set<SpriteSheetProvider> providers) throws SlickException {
        System.out.println("constructor called with " + providers);
        for (SpriteSheetProvider provider : new ArrayList<SpriteSheetProvider>(providers)) {
            for (String spriteSheetName : provider.getAllSpriteSheetNames()) {
                loadSpriteSheet(spriteSheetName);
            }
        }
    }

    public void loadSpriteSheet(String spriteSheetName) throws SlickException {
        // treat green as transparent
        SpriteSheet ss = new SpriteSheet(spriteSheetName, 24, 32, new Color(32, 156, 0));
        ss.setAlpha(1f);
        spriteSheetMap.put(spriteSheetName, ss);
    }

    public SpriteSheet spriteSheetForName(String spriteSheetName) {
        return spriteSheetMap.get(spriteSheetName);
    }

}
