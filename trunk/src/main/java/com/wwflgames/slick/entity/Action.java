package com.wwflgames.slick.entity;

import com.wwflgames.slick.entity.Component;

public abstract class Action extends Component {

    private boolean isActionComplete;

    public Action(String id) {
        super(id);
        isActionComplete = false;
    }

    public boolean isActionComplete() {
        return isActionComplete;
    }

    protected void actionComplete() {
        isActionComplete = true;
    }

}
