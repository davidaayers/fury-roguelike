package com.wwflgames.fury.gamestate.battle;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.player.item.UsableItem;
import com.wwflgames.slick.entity.ui.PopupRenderer;
import com.wwflgames.slick.entity.ui.SelectableList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.util.TextUtils.centerText;

public class UseItemPopup extends PopupRenderer {
    private SelectableList list;
    private Player player;
    private BattleRound battleRound;

    public UseItemPopup(String id) {
        super(id, 300, 400);
        initComponents();
    }

    private void initComponents() {
        list = new SelectableList(5, x + 10, y + 70, popupWidth - 20, Color.green, Color.black, Color.blue,
                new SelectableList.ItemSelectedListener() {
                    @Override
                    public void itemSelected(Object o) {
                    }

                    @Override
                    public void selectionConfirmed(Object o) {
                        UsableItem item = (UsableItem) o;
                        item.use(player, battleRound);
                        player.removeItem(item);
                        setVisible(false);
                    }
                });
    }

    public void showForPlayer(Player player, BattleRound battleRound) {
        this.player = player;
        this.battleRound = battleRound;

        List<SelectableList.SelectableItem> selectables = new ArrayList<SelectableList.SelectableItem>();
        List<UsableItem> itemsList = player.getUsableItems();
        for (UsableItem item : itemsList) {
            selectables.add(list.createSelectableItem(item, item.getName(), item.getDescription()));
        }

        list.setList(selectables.toArray(new SelectableList.SelectableItem[selectables.size()]));

        setVisible(true);
    }


    @Override
    protected void doRender(Graphics gr, int popupX, int popupY) {

        int ry = y;
        gr.setColor(Color.blue);
        centerText(x, this.popupWidth, gr, "Choose item to use", ry);
        ry += 20;
        gr.setColor(Color.gray);
        centerText(x, this.popupWidth, gr, "Enter to choose an item,", ry);
        ry += 20;
        centerText(x, this.popupWidth, gr, "enter again to confirm", ry);
        ry += 20;
        list.render(gr);
    }

    @Override
    protected void doUpdate(int delta) {
        list.update(delta);
    }

    @Override
    protected boolean doKeyPressed(int key, char c) {
        if (list.keyPressed(key, c)) {
            return true;
        }
        return false;
    }


}
