package com.wwflgames.slick.entity;

import org.newdawn.slick.geom.Vector2f;

public class MoveToAction extends NotifyingAction {

    private float finalX;
    private float finalY;
    private float speed;

    public MoveToAction(String id, float finalX, float finalY, float speed, ActionFinishedNotifier notifier) {
        super(id, notifier);
        this.finalX = finalX;
        this.finalY = finalY;
        this.speed = speed;
    }

    @Override
    public void update(int delta) {
        // grab our owner's current x,y
        Vector2f pos = owner.getPosition();

        float x = pos.x;
        float y = pos.y;

        float newX;
        float newY;

        boolean isLeft = x < finalX;
        boolean isDown = y < finalY;

        if (isLeft) {
            newX = x + delta * speed;
            if (newX > finalX) {
                newX = finalX;
            }
        } else {
            newX = x - delta * speed;
            if (newX < finalX) {
                newX = finalX;
            }
        }

        if (isDown) {
            newY = y + delta * speed;
            if (newY > finalY) {
                newY = finalY;
            }
        } else {
            newY = y - delta * speed;
            if (newY < finalY) {
                newY = finalY;
            }
        }

        owner.setPosition(new Vector2f(newX, newY));

        // have we arrived?
        if (newX == finalX && newY == finalY) {
            actionComplete();
        }
    }
}
