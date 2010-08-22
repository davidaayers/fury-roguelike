package com.wwflgames.slick.entity;

public abstract class SimpleRenderer extends Renderer {
    public SimpleRenderer(String id) {
        super(id);
    }

    /**
     * Empty implementation, for those renderers that
     * don't need to perform any updates
     */
    @Override
    public void update(int delta) {
    }
}
