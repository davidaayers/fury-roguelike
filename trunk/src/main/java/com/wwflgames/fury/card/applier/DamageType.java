package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.mob.Stat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum DamageType {

    MELEE(null),
    MAGIC(null),
    POISON(null),
    CRUSH(MELEE,Stat.STRENGTH),
    SLASH(MELEE,Stat.STRENGTH,Stat.DEXTERITY),
    PIERCE(MELEE,Stat.DEXTERITY),
    FIRE(MAGIC,Stat.MAGIC),
    ICE(MAGIC,Stat.MAGIC)
    ;

    private DamageType parentType;
    private Stat[] buffedBy;

    DamageType(DamageType parentType, Stat ... buffedBy) {
        this.parentType = parentType;
        this.buffedBy = buffedBy;
    }

    public boolean isType(DamageType type) {
        return this == type || ( parentType != null && parentType == type );
    }

    public Stat[] getBuffedBy() {
        // buffed by can also come from the parentType, but each stat can only
        // exist once (i.e. they dont get buffed twice because a parent type has the same buffed by
        // as the child).
        Set<Stat> stats = new HashSet<Stat>();
        if ( buffedBy != null ) {
            stats.addAll(Arrays.asList(buffedBy));
        }
        if ( parentType.buffedBy != null ) {
            stats.addAll(Arrays.asList(parentType.buffedBy));
        }
        return stats.toArray(new Stat[stats.size()]);
    }
}
