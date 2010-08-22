package com.wwflgames.slick.entity;

import com.wwflgames.slick.entity.Action;

public class DisplayForTimeAction extends Action {

    private int time;
    private int counter;

    public DisplayForTimeAction(String id, int time) {
        super(id);
        this.time = time;
        counter = 0;
    }

    @Override
    public void update(int delta) {
        if (!owner.shouldRemove()) {
            counter += delta;
            if (counter > time) {
                owner.setRemove(true);
            }
        }
    }
}
