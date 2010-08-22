package com.wwflgames.slick.entity;

public abstract class NotifyingAction extends Action {

    private ActionFinishedNotifier notifier;

    public NotifyingAction(String id, ActionFinishedNotifier notifier) {
        super(id);
        this.notifier = notifier;
    }

    protected void actionComplete() {
        super.actionComplete();
        notifier.actionComplete();
    }
}
