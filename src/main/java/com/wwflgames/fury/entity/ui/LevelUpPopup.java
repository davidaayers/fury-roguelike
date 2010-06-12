package com.wwflgames.fury.entity.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class LevelUpPopup extends PopupRenderer {
    private SelectableList list;

    public LevelUpPopup(String id) {
        super(id, 600, 400);
        initComponents();
    }

    private void initComponents() {
        list = new SelectableList(5, x + popupWidth / 2, y + 10, popupWidth / 2, Color.green, Color.black, Color.blue,
                new SelectableList.ItemSelectedListener() {
                    @Override
                    public void itemSelected(Object o) {
                    }

                    @Override
                    public void selectionConfirmed(Object o) {
                    }
                });
        list.setList(new SelectableList.SelectableItem[] {
                list.createSelectableItem(new Object(),"Test","test"),
                list.createSelectableItem(new Object(),"Test2","test2"),
                list.createSelectableItem(new Object(),"Test3","test3"),
                list.createSelectableItem(new Object(),"Test4","test4"),
                list.createSelectableItem(new Object(),"Test5","test5"),
                list.createSelectableItem(new Object(),"Test6","test6"),
                list.createSelectableItem(new Object(),"Test7","test7"),
                list.createSelectableItem(new Object(),"Test8","test8"),
        });
    }

    @Override
    protected void doRender(Graphics gr, int popupX, int popupY) {
        list.render(gr);
    }

    @Override
    protected void doUpdate(int delta) {
        list.update(delta);
    }

    @Override
    protected boolean doKeyPressed(int key, char c) {
        if ( list.keyPressed(key,c) ) {
            return true;
        }
        return false;
    }
}
