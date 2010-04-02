package com.wwflgames.fury.entity;

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
