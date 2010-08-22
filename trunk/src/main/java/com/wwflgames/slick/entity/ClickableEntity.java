package com.wwflgames.slick.entity;

import com.wwflgames.slick.entity.Entity;
import com.wwflgames.slick.entity.EntityMouseHandler;

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
