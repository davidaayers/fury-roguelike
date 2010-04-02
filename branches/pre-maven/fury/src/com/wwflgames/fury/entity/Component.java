package com.wwflgames.fury.entity;

public abstract class Component {

    protected String id;
    protected Entity owner;

    public Component(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void installOwner(Entity owner) {
        this.owner = owner;
    }

    public abstract void update(int delta);

}
