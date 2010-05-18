package com.wwflgames.fury.card.applier;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.mob.Mob;

public interface Applier {
    void applyTo(Card card, Mob usedBy, Mob usedAgainst, BattleRound battleRound);
    String description();
}
