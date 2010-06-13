package com.wwflgames.fury.player.item;

import com.wwflgames.fury.battle.BattleRound;
import com.wwflgames.fury.battle.CardResult;
import com.wwflgames.fury.mob.Stat;
import com.wwflgames.fury.player.Player;

public class HealthPotion extends UsableItem {
    
    private int amountHealed;

    public HealthPotion(int amountHealed) {
        super("Health Potion", "Heals " + amountHealed);
        this.amountHealed = amountHealed;
    }

    @Override
    public void use(Player player, BattleRound battleRound) {
        player.modifyStatValue(Stat.HEALTH,amountHealed);
        battleRound.addBattleResult(player,CardResult.newCardPlayedResult("{0} used Health Potion",player));
        battleRound.addBattleResult(player,
                CardResult.newBuffResult("{0] gained " + amountHealed + " health", player));
    }
}
