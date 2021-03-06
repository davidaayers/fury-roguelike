package com.wwflgames.fury.player.perk;

import com.wwflgames.fury.player.Player;

public class HandIncreasePerk extends Perk {
    private int numCards;

    public HandIncreasePerk(String name, String description, Perk prerequisitePerk, int numCards) {
        super(name, description, prerequisitePerk);
        this.numCards = numCards;
    }

    @Override
    public void applyPerk(Player player) {
        player.getHand().setMaxHandSize(player.getHand().getMaxHandSize() + numCards);
        player.getHand().drawToMax();
    }
}
