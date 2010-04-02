package com.wwflgames.fury.entity;

import org.newdawn.slick.Graphics;

public abstract class Renderer extends Component {

    public Renderer(String id) {
        super(id);
    }

    public abstract void render(Graphics gr);

}
