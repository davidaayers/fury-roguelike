package com.wwflgames.fury.player;

public abstract class Perk {
    private String name;
    private String description;
    private Perk prerequisitePerk;

    public Perk(String name, String description, Perk prerequisitePerk) {
        this.name = name;
        this.description = description;
        this.prerequisitePerk = prerequisitePerk;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasPrerequisite() {
        return prerequisitePerk != null;
    }

    public Perk getPrerequisitePerk() {
        return prerequisitePerk;
    }

    public abstract void applyPerk(Player player);
}
