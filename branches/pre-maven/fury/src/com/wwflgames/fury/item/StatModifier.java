package com.wwflgames.fury.item;

import com.wwflgames.fury.mob.Stat;

public class StatModifier {
    private Stat statModified;
    private int modifedBy;

    public StatModifier(Stat statModified, int modifedBy) {
        this.statModified = statModified;
        this.modifedBy = modifedBy;
    }

    public Stat getStatModified() {
        return statModified;
    }

    public int getModifedBy() {
        return modifedBy;
    }
}
