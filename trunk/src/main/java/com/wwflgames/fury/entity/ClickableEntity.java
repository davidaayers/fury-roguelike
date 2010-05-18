package com.wwflgames.fury.entity;

import org.newdawn.slick.MouseListener;

public class ClickableEntity extends Entity {

    private EntityMouseHandler mouseHandler;

    public ClickableEntity(String id, EntityMouseHandler mouseHandler) {
        super(id);
        this.mouseHandler = mouseHandler;
    }

    public EntityMouseHandler getMouseHandler() {
        return mouseHandler;
    }
}
