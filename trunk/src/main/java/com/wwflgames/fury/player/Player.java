package com.wwflgames.fury.player;

import com.wwflgames.fury.mob.Mob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player extends Mob {

    private Profession profession;
    private int level = 1;
    private int exp = 0;
    private boolean levelUp = false;
    private int unspentActionPoints = 0;
    private List<Perk> perks = new ArrayList<Perk>();

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
        perks.add(perk);
    }

    public List<Perk> getPerks() {
        return Collections.unmodifiableList(perks);
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
