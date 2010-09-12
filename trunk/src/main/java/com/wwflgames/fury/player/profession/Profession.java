package com.wwflgames.fury.player.profession;

import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.mob.StatHolder;
import com.wwflgames.fury.player.Player;
import com.wwflgames.fury.player.perk.Perk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profession implements StatHolder {
    private String name;
    private String spriteSheet;
    private Map<Stat, Integer> starterStats = new HashMap<Stat, Integer>();
    private List<Perk> availablePerks = new ArrayList<Perk>();
    private List<Perk> startingPerks = new ArrayList<Perk>();

    public Profession(String name, String spriteSheet) {
        this.name = name;
        this.spriteSheet = spriteSheet;
    }

    public String getName() {
        return name;
    }

    public String getSpriteSheet() {
        return spriteSheet;
    }

    public void setStatValue(Stat stat, Integer value) {
        starterStats.put(stat, value);
    }

    public void setupPlayer(Player player) {
        for (Stat stat : starterStats.keySet()) {
            player.setStatValue(stat, starterStats.get(stat));
        }
        for ( Perk perk : startingPerks ) {
            player.addPerk(perk);
        }
    }

    public List<Perk> eligiblePerksForPlayer(Player player) {
        List<Perk> perks = new ArrayList<Perk>();
        List<Perk> playerPerks = player.getPerks();

        for ( Perk perk : availablePerks) {
            if ( (!perk.hasPrerequisite() || playerPerks.contains(perk.getPrerequisitePerk())) &&
                    !playerPerks.contains(perk)) {
                perks.add(perk);
            }
        }

        return perks;
    }

    // package private, to be called only from ProfessionFactory
    void addAvailablePerk(Perk perk) {
        availablePerks.add(perk);
    }

    void addStartingPerk(Perk perk) {
        startingPerks.add(perk);
    }

    @Override
    public String toString() {
        return "Profession{" +
                "name='" + name + '\'' +
                ", spriteSheet='" + spriteSheet + '\'' +
                '}';
    }
}
