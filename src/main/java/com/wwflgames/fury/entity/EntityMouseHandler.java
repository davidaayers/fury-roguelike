package com.wwflgames.fury.entity;

public interface EntityMouseHandler {
    
    void mouseMoved(int oldx, int oldy, int newx, int newy);

    /**
     * Returns true if this handler handled the event
     * @param button
     * @param x
     * @param y
     * @param clickCount
     * @return
     */
    boolean mouseClicked(int button, int x, int y, int clickCount); 
}
