package com.wwflgames.fury.player;

import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.player.item.Item;
import com.wwflgames.fury.player.item.UsableItem;
import com.wwflgames.fury.player.perk.Perk;
import com.wwflgames.fury.player.profession.Profession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player extends Mob {

    private Profession profession;
    private int level = 1;
    private int exp = 0;
    private boolean levelUp = false;
    private List<Perk> perks = new ArrayList<Perk>();
    private List<Item> items = new ArrayList<Item>();

    public Player(String name, Profession profession) {
        super(name);
        this.profession = profession;
    }

    public Profession getProfession() {
        return profession;
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

    public void addPerk(Perk perk) {
        perk.applyPerk(this);
        perks.add(perk);
        levelUp = false;
    }

    public List<Perk> getPerks() {
        return Collections.unmodifiableList(perks);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<UsableItem> getUsableItems() {
        List<UsableItem> usableItems = new ArrayList<UsableItem>();
        for ( Item item : items ) {
            if ( item instanceof UsableItem ) {
                usableItems.add((UsableItem) item);
            }
        }
        return Collections.unmodifiableList(usableItems);
    }

    private void checkLevelUp() {
        int expPerLevel = 5;
        int currentLevelExp = level * expPerLevel;
        if ( exp >= currentLevelExp ) {
            levelUp = true;
            level++;
        }
    }
}
