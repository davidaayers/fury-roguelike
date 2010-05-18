package com.wwflgames.fury.card;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.card.applier.Applier;
import com.wwflgames.fury.mob.Mob;

public class Card {

    private String name;
    // all the appliers that should be applied to the mob that played this card
    private Applier[] usedByAppliers;
    // all the appliers that should be applied to the mob that this card was played on
    private Applier[] usedAgainstAppliers;

    // how many targets this card has. 0 = none
    private int numTargets = 0;

    public Card(String name, Applier[] usedByAppliers, Applier[] usedAgainstAppliers, int numTargets) {
        this.name = name;
        this.usedByAppliers = usedByAppliers;
        this.usedAgainstAppliers = usedAgainstAppliers;
        this.numTargets = numTargets;
    }

    public String getName() {
        return name;
    }

    public Card usedBy(Mob mob, BattleRound battleRound) {
        for ( Applier applier:usedByAppliers ) {
            applier.applyTo(this,mob,null,battleRound);
        }
        return this;
    }

    public Card usedAgainst(Mob usedBy, Mob usedAgainst, BattleRound battleRound) {
        for ( Applier applier:usedAgainstAppliers ) {
            applier.applyTo(this,usedBy,usedAgainst,battleRound);
        }
        return this;
    }

    public boolean isTargetable() {
        return numTargets != 0;
    }

    public Applier[] getUsedByAppliers() {
        return usedByAppliers;
    }

    public Applier[] getUsedAgainstAppliers() {
        return usedAgainstAppliers;
    }

    public int getNumTargets() {
        return numTargets;
    }
}
