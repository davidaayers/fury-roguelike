package com.wwflgames.fury.battle;

import com.wwflgames.fury.item.Item;
import com.wwflgames.fury.item.effect.DebuffEffect;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BattleSystem {

    private Battle battle;
    private int round;
    private boolean battleOver = false;
    private boolean playerWon = false;

    public BattleSystem(Battle battle) {
        this.battle = battle;
    }

    public void startBattle() {
        Log.debug("Starting battle...");
        round = 1;
        // prepare all of the mobs for battle. This will set their
        // battle stats. Also, shuffle all of their decks.
        for (Mob mob : battle.getAllBattleParticipants()) {
            mob.prepareForBattle();
            mob.getItemDeck().shuffle();
        }
    }

    public BattleRound performBattleRound(Monster monster) {
        // increment the battle Round
        round++;
        BattleRound battleRound = new BattleRound(this.round);
        Log.debug("=========( Round " + this.round + " )==============");


        // see who got initiate and let them go first
        if (battle.isPlayerInitiate()) {
            doPlayerRoundAndCheckIfPlayerWon(monster, battleRound);
        } else {
            doEnemyRoundAndCheckIfPlayerLost(battleRound);
        }

        // now, let the other side attack
        if (battle.isPlayerInitiate()) {
            doEnemyRoundAndCheckIfPlayerLost(battleRound);
        } else {
            doPlayerRoundAndCheckIfPlayerWon(monster, battleRound);
        }

        Log.debug("Round " + this.round + " is over");
        return battleRound;
    }

    private void doPlayerRoundAndCheckIfPlayerWon(Monster monster, BattleRound battleRound) {
        doNextItemInDeck(battle.getPlayer(), monster, battleRound);
        maybeApplyDebuffs(battle.getPlayer(),battleRound);
        maybeRemoveDebuffs(battle.getPlayer(),battleRound);
        removeDeadMonstersFromBattle();

        if (battle.allEnemiesDead()) {
            playerWon();
        }
    }

    private void doEnemyRoundAndCheckIfPlayerLost(BattleRound battleRound) {
        for (Mob enemy : battle.getEnemies()) {
            doNextItemInDeck(enemy, battle.getPlayer(), battleRound);
            maybeApplyDebuffs(enemy,battleRound);
            maybeRemoveDebuffs(enemy,battleRound);
        }

        if (battle.getPlayer().isDead()) {
            playerLost();
        }
    }

    private void doNextItemInDeck(Mob attacker, Mob defender, BattleRound battleRound) {
        Log.debug("Next item in itemDeck, attacker = " + attacker.name() + ", defender = " + defender.name());
        // grab the next item from the attackers itemDeck
        Item item = attacker.getItemDeck().nextItem();

        Log.debug("Item chosen from itemDeck is " + item.name());

        // set the item used on the round for this mob
        battleRound.addItemUsedBy(attacker,item);


        ItemUsage itemUsage = new ItemUsage(item, attacker);
        // change order here -- do attacks first, then buffs. That way items that do both buffs and
        // attacks have the buffs apply to the next attack, rather than this attack
        item.usedAgainst(attacker, defender, itemUsage).usedBy(attacker, itemUsage);
        // if the item killed the defender, add a message about it
        if (defender.isDead()) {
            String deathBlow = defender.name() + " was killed by " + item.name();
            itemUsage.addAtFront(ItemEffectResult.newDeathItemEffect(deathBlow, defender));
        }

        for ( ItemEffectResult itemResult : itemUsage.get() ) {
            battleRound.addBattleResult(attacker,itemResult);
        }
    }

    private void maybeApplyDebuffs(Mob mob, BattleRound battleRound) {
        for ( DebuffEffect debuff : mob.getDebuffs() ) {
            if ( debuff.appliesEveryRound() ) {

            }
        }

    }

    private void maybeRemoveDebuffs(Mob mob, BattleRound battleRound) {
        for ( DebuffEffect debuff : mob.getDebuffs() ) {
            if ( !debuff.stillActive() ) {
                debuff.woreOff(mob);
            }
        }
    }


    private void removeDeadMonstersFromBattle() {
        List<Mob> enemiesToRemove = new ArrayList<Mob>();
        for (Mob enemy : battle.getEnemies()) {
            Log.debug("Seeing if " + enemy.name() + " is dead");
            if (enemy.isDead()) {
                Log.debug(enemy + " was dead, removing");
                enemiesToRemove.add(enemy);
            }
        }

        for (Mob enemyToRemove : enemiesToRemove) {
            battle.removeEnemy(enemyToRemove);
        }
    }

    private void playerWon() {
        battleOver = true;
        playerWon = true;
        
        Log.debug("Player won :)");
    }

    private void playerLost() {
        battleOver = true;
        playerWon = false;
        Log.debug("Player lost :(");
    }

    public boolean isBattleOver() {
        return battleOver;
    }

    public boolean didPlayerWin() {
        return playerWon;
    }

    public int getRound() {
        return round;
    }
}
