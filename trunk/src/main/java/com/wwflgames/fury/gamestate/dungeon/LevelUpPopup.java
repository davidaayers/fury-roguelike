package com.wwflgames.fury.gamestate.dungeon;

import com.wwflgames.fury.player.Perk;
import com.wwflgames.fury.player.Player;
import com.wwflgames.slick.entity.ui.PopupRenderer;
import com.wwflgames.slick.entity.ui.SelectableList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

import static com.wwflgames.fury.util.TextUtils.centerText;

public class LevelUpPopup extends PopupRenderer {
    private SelectableList list;
    private Player player;

    public LevelUpPopup(String id) {
        super(id, 600, 400);
        initComponents();
    }

    private void initComponents() {
        list = new SelectableList(5, x + popupWidth / 2, y + 70, popupWidth / 2 - 10, Color.green, Color.black, Color.blue,
                new SelectableList.ItemSelectedListener() {
                    @Override
                    public void itemSelected(Object o) {
                    }

                    @Override
                    public void selectionConfirmed(Object o) {
                        player.addPerk((Perk) o);
                        setVisible(false);
                    }
                });
    }

    public void showPopup(Player player) {
        this.player = player;
        List<SelectableList.SelectableItem> selectables = new ArrayList<SelectableList.SelectableItem>();
        List<Perk> perkList = player.getProfession().eligiblePerksForPlayer(player);
        for (Perk perk : perkList) {
            selectables.add(list.createSelectableItem(perk, perk.getName(), perk.getDescription()));
        }

        list.setList(selectables.toArray(new SelectableList.SelectableItem[selectables.size()]));
        setVisible(true);
    }


    @Override
    protected void doRender(Graphics gr, int popupX, int popupY) {

        int ry = y;
        gr.setColor(Color.blue);
        centerText(x, this.popupWidth, gr, "LEVEL UP!", ry);
        ry += 20;
        centerText(x, this.popupWidth, gr, "Choose a new perk", ry);
        ry += 20;
        gr.setColor(Color.gray);
        centerText(x, this.popupWidth, gr, "Enter to choose a perk, enter again to confirm", ry);
        ry += 20;

        gr.setColor(Color.green);
        gr.drawString("Current Perks:", x + 10, ry);
        ry += 20;

        // render the player's current perks
        for (Perk perk : player.getPerks()) {
            gr.setColor(Color.black);
            gr.drawString(perk.getName(), x + 10, ry);
            ry += 15;
            gr.setColor(Color.blue);
            gr.drawString(perk.getDescription(), x + 10, ry);
            ry += 15;
        }

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
