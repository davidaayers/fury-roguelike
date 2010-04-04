package com.wwflgames.fury.player;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.ItemDeck;
import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Mob {

    private Profession profession;
    private Map<Integer, ItemDeck> deckMap = new HashMap<Integer, ItemDeck>();
    private List<Item> allItems = new ArrayList<Item>();
    private int level = 1;
    private int exp = 0;
    private boolean levelUp = false;
    private int unspentActionPoints = 0;

    public Player(String name, Profession profession) {
        super(name);
        this.profession = profession;
    }

    public Profession getProfession() {
        return profession;
    }

    public void installDeck(int deckNo, ItemDeck deck) {
        deckMap.put(deckNo, deck);
    }

    public ItemDeck deckForDeckNo(int deckNo) {
        return deckMap.get(deckNo);
    }

    public void setDefaultDeck(int deckNo) {
        setDeck(deckMap.get(deckNo));
    }

    public void addItem(Item item) {
        allItems.add(item);
    }

    public List<Item> getAllItems() {
        return allItems;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public void addExp(int exp) {
        this.exp = this.exp + exp;
        // see if we leveled up
        checkLevelUp();
    }

    public boolean hasLeveledUp() {
        return levelUp;
    }

    private void checkLevelUp() {
        int expPerLevel = 5;
        int currentLevelExp = level * expPerLevel;
        if ( exp >= currentLevelExp ) {
            levelUp = true;
            unspentActionPoints+=5;
            level++;
        }
    }
}
