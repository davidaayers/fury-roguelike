package com.wwflgames.fury.entity.ui;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.entity.EntityKeyHandler;
import com.wwflgames.fury.entity.Renderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class PopupRenderer extends Renderer implements EntityKeyHandler {
    private boolean visible;
    protected int popupWidth = 400;
    protected int popupHeight = 400;
    protected int x;
    protected int y;

    public PopupRenderer(String id, int popupWidth, int popupHeight) {
        super(id);
        this.popupWidth = popupWidth;
        this.popupHeight = popupHeight;
        x = Fury.GAME_WIDTH / 2 - popupWidth /2;
        y = Fury.GAME_HEIGHT / 2 - popupHeight /2;
    }

    @Override
    public void render(Graphics gr) {
        if (visible) {
            Color oldColor = gr.getColor();
            // draw a semi-transparent layer over the whole playfield
            Color overlay = new Color(Color.black.r, Color.black.g, Color.black.g, .5f);
            gr.setColor(overlay);
            gr.fillRect(0, 0, Fury.GAME_WIDTH, Fury.GAME_HEIGHT);

            // draw a box for our pop-up
            gr.setColor(Color.white);
            gr.fillRoundRect(x, y, popupWidth, popupHeight, 8);
            gr.setColor(Color.gray);
            gr.drawRoundRect(x, y, popupWidth, popupHeight, 8);

            doRender(gr,x,y);

            gr.setColor(oldColor);
        }
    }

    protected void doRender(Graphics gr, int popupX, int popupY) {
    }

    @Override
    public void update(int delta) {
        doUpdate(delta);
    }

    protected void doUpdate(int delta) {
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean keyPressed(int key, char c) {
        if ( !visible) {
            return false;
        }

        if ( key == Input.KEY_ESCAPE ) {
            visible = false;
            return true;
        }

        return doKeyPressed(key,c);
    }

    protected boolean doKeyPressed(int key, char c) {
        return false;
    }
}
