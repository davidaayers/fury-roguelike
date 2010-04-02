package com.wwflgames.fury.gamestate;

import com.wwflgames.fury.Fury;
import com.wwflgames.fury.entity.Entity;
import com.wwflgames.fury.entity.EntityManager;
import com.wwflgames.fury.entity.ItemRenderer;
import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.main.AppState;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

public class ManageDeckGameState extends BasicGameState {

    private AppState appState;
    private GameContainer container;
    private StateBasedGame game;
    private List<ItemContainer> currentDeckItems = new ArrayList<ItemContainer>();
    private List<ItemContainer> allPlayerItems = new ArrayList<ItemContainer>();
    private List<MouseOverArea> mouseOvers = new ArrayList<MouseOverArea>();
    private Image plusImage;
    private Image minusImage;
    private boolean shouldRebuildButtons;
    private int currentDeckNo;
    private EntityManager entityManager;
    private UnicodeFont font;

    public ManageDeckGameState(AppState appState) {
        this.appState = appState;
    }

    @Override
    public int getID() {
        return Fury.MANAGE_DECK_STATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.container = container;
        this.game = game;

        plusImage = new Image("plus.png");
        minusImage = new Image("minus.png");

        java.awt.Font jFont = new java.awt.Font("Verdana", java.awt.Font.PLAIN, 12);
        font = new UnicodeFont(jFont);
        font.getEffects().add(new ColorEffect(java.awt.Color.white));
        font.addAsciiGlyphs();
        font.loadGlyphs();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        currentDeckNo = 1;
        choosePlayerDeck(currentDeckNo);
        createAllItemList();
        createButtons();

        entityManager = new EntityManager(container, game);
    }

    private void createButtons() {
        int x = 10;
        int y = 200;
        mouseOvers.clear();
        for (ItemContainer itemContainer : currentDeckItems) {
            final Item item = itemContainer.getItem();
            MouseOverArea moa = new MouseOverArea(container, minusImage, x, y, new ComponentListener() {
                @Override
                public void componentActivated(AbstractComponent source) {
                    removeItemFromCurrentDeck(item);
                }
            });
            moa.setMouseOverColor(Color.red);
            mouseOvers.add(moa);
            y += 20;
        }

        x = 500;
        y = 200;
        for (ItemContainer itemContainer : allPlayerItems) {
            final Item item = itemContainer.getItem();
            MouseOverArea moa = new MouseOverArea(container, plusImage, x, y, new ComponentListener() {
                @Override
                public void componentActivated(AbstractComponent source) {
                    addItemToCurrentDeck(item);
                }
            });
            moa.setMouseOverColor(Color.red);
            mouseOvers.add(moa);
            y += 20;
        }


    }

    private void addItemToCurrentDeck(Item item) {
        moveFrom(item, allPlayerItems, currentDeckItems);
        shouldRebuildButtons = true;
    }

    private void removeItemFromCurrentDeck(Item item) {
        moveFrom(item, currentDeckItems, allPlayerItems);
        shouldRebuildButtons = true;
    }

    private void moveFrom(Item item, List<ItemContainer> listA, List<ItemContainer> listB) {
        ItemContainer container = findContainerForItem(item, listA);
        container.setQty(container.getQty() - 1);
        if (container.getQty() == 0) {
            listA.remove(container);
        }

        ItemContainer container2 = findContainerForItem(item, listB);
        if (container2 != null) {
            container2.incQty();
        } else {
            ItemContainer newItem = new ItemContainer(item);
            newItem.incQty();
            listB.add(newItem);
        }
    }

    private void createAllItemList() {
        createItemContainerList(appState.getPlayer().getAllItems(), allPlayerItems);

        // now, remove from the list any items that are in the current deck
        for (ItemContainer itemContainer : currentDeckItems) {
            ItemContainer c = findContainerForItem(itemContainer.getItem(), allPlayerItems);
            if (c != null) {
                c.setQty(c.getQty() - itemContainer.getQty());
                if (c.getQty() == 0) {
                    allPlayerItems.remove(c);
                }
            }
        }
    }

    private void choosePlayerDeck(int deckNo) {
        createItemContainerList(appState.getPlayer().deckForDeckNo(deckNo).getDeck(), currentDeckItems);
    }

    private void createItemContainerList(List<Item> items, List<ItemContainer> itemContainers) {
        itemContainers.clear();
        for (Item item : items) {
            ItemContainer container = findContainerForItem(item, itemContainers);
            if (container == null) {
                container = new ItemContainer(item);
                container.incQty();
                itemContainers.add(container);
            } else {
                container.incQty();
            }
        }
    }

    private ItemContainer findContainerForItem(Item item, List<ItemContainer> itemContainers) {
        for (ItemContainer container : itemContainers) {
            if (container.getItem() == item) {
                return container;
            }
        }
        return null;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        // render left side, which contains all items in the current deck
        drawItems(container, g, currentDeckItems, 35, 200);

        drawItems(container, g, allPlayerItems, 520, 200);

        for (MouseOverArea moa : mouseOvers) {
            moa.render(container, g);
        }

        entityManager.render(g);

    }

    private void drawItems(GameContainer container, Graphics g, List<ItemContainer> items, int startX, int startY) {
        int x = startX;
        int y = startY;

        for (ItemContainer itemContainer : items) {
            String str = itemContainer.getItem().name() + " " + itemContainer.getQty();
            int strWidth = g.getFont().getWidth(str);
            g.setColor(Color.white);
            g.drawString(str, x, y);
            // if the mouse is over this item, put a line around it
            int mouseX = container.getInput().getMouseX();
            int mouseY = container.getInput().getMouseY();
            if (mouseX >= x && mouseX <= x + strWidth &&
                    mouseY >= y && mouseY <= y + 20) {
                g.setColor(Color.yellow);
                g.drawRect(x, y, strWidth, 20);
                showCard(g, itemContainer);
            }
            y += 20;
        }
    }

    private void showCard(Graphics g, ItemContainer itemContainer) {
        new Entity("cardEntity")
                .setPosition(new Vector2f(320, 200))
                .addComponent(new ItemRenderer(itemContainer.getItem(), font))
                .render(g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        if (shouldRebuildButtons) {
            // re-create the buttons
            createButtons();
            shouldRebuildButtons = false;
        }
        entityManager.update(delta);

    }

    @Override
    public void keyPressed(int key, char c) {
        if (c == ' ') {
            // go back to dungeon screen
            buildNewDeck();
            game.enterState(Fury.DUNGEON_GAME_STATE);
        }
    }

    private void buildNewDeck() {
        currentDeckNo = 1;
        ItemDeck deck = appState.getPlayer().deckForDeckNo(currentDeckNo);
        deck.getDeck().clear();
        for (ItemContainer container : currentDeckItems) {
            Item item = container.getItem();
            for (int cnt = 0; cnt < container.getQty(); cnt++) {
                deck.getDeck().add(item);
            }
        }
    }

    private class ItemContainer {
        private Item item;
        private int qty;

        private ItemContainer(Item item) {
            this.item = item;
        }

        public Item getItem() {
            return item;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public void incQty() {
            qty++;
        }


    }
}
