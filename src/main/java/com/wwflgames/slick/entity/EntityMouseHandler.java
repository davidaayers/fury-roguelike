package com.wwflgames.slick.entity;

public interface EntityMouseHandler {
    
    void mouseMoved(int oldx, int oldy, int newx, int newy);

    boolean mouseClicked(int button, int x, int y, int clickCount); 
}
