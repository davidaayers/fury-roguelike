package com.wwflgames.fury.player.item;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.player.Player;

public abstract class UsableItem extends Item {
    public UsableItem(String name, String description) {
        super(name, description);
    }

    public abstract void use(Player player,BattleRound battleRound);
}
