package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.mob.Stat;

import java.util.*;

public enum DamageType {

    MELEE(null,null),
    MAGIC(null,null),
    POISON(null,null),
    // crush damage destroys armor -- one point of armor for each point of damage mitigated
    // by the armor.
    CRUSH(MELEE,DamageMitigator.newDestroyableMitigator(Stat.ARMOR,1.0,1.0),Stat.STRENGTH),
    // slash damage is completely mitigated by armor, and the armor is not destroyed
    SLASH(MELEE,DamageMitigator.newNonDestroyedMitigator(Stat.ARMOR,1.0),Stat.STRENGTH,Stat.DEXTERITY),
    // pierce damage is only 50% mitigated by armor, and the armor is not destroyed
    PIERCE(MELEE,DamageMitigator.newNonDestroyedMitigator(Stat.ARMOR,.5),Stat.DEXTERITY),
    FIRE(MAGIC,null,Stat.MAGIC),
    ICE(MAGIC,null,Stat.MAGIC)
    ;

    private DamageType parentType;
    private DamageMitigator damageMitigator;
    private Stat[] buffedBy;

    DamageType(DamageType parentType, DamageMitigator damageMitigator, Stat ... buffedBy) {
        this.parentType = parentType;
        this.damageMitigator = damageMitigator;
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

    public DamageMitigator getDamageMitigator() {
        return damageMitigator;
    }

    public static List<DamageType> findAllChildrenOfType(DamageType type) {
        List<DamageType> matchingTypes = new ArrayList<DamageType>();
        for ( DamageType damageType : DamageType.values() ) {
            if ( damageType.parentType == type ) {
                matchingTypes.add(damageType);
            }
        }
        return matchingTypes;
    }
}
