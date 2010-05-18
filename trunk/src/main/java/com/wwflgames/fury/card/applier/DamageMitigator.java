package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.mob.Stat;

public class DamageMitigator {
    private Stat mitigatingStat;
    private boolean isDestroyed;
    private double destroyedPct;
    private double absorbedPct;

    public DamageMitigator(Stat mitigatingStat, boolean destroyed, double destroyedPct, double absorbedPct) {
        this.mitigatingStat = mitigatingStat;
        isDestroyed = destroyed;
        this.destroyedPct = destroyedPct;
        this.absorbedPct = absorbedPct;
    }

    public static DamageMitigator newDestroyableMitigator(Stat stat, double destroyedPct, double absorbedPct ) {
        return new DamageMitigator(stat,true,destroyedPct,absorbedPct);
    }

    public static DamageMitigator newNonDestroyedMitigator(Stat stat, double absorbedPct) {
        return new DamageMitigator(stat,false,0,absorbedPct);
    }

    public Stat getMitigatingStat() {
        return mitigatingStat;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public double getDestroyedPct() {
        return destroyedPct;
    }

    public double getAbsorbedPct() {
        return absorbedPct;
    }
}
