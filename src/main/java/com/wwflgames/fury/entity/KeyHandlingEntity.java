package com.wwflgames.fury.entity;

public class KeyHandlingEntity extends Entity {
    private EntityKeyHandler keyHandler;

    public KeyHandlingEntity(String id, EntityKeyHandler keyHandler) {
        super(id);
        this.keyHandler = keyHandler;
    }

    public boolean keyPressed(int key, char c) {
        return keyHandler.keyPressed(key,c);
    }
}
