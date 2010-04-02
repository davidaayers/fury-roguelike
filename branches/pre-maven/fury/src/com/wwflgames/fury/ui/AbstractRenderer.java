package com.wwflgames.fury.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public abstract class AbstractRenderer {

    protected GameContainer container;
    protected StateBasedGame game;

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        this.game = game;
    }

    public abstract void render(Graphics g) throws SlickException;

    public abstract void update(int delta) throws SlickException;


}
