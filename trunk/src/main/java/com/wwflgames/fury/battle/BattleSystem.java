package com.wwflgames.fury.battle;

import com.wwflgames.fury.card.Card;
import com.wwflgames.fury.card.statuseffect.StatusEffect;
import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;
import com.wwflgames.fury.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BattleSystem {

    private Battle battle;
    private int round;
    private boolean battleOver;
    private boolean playerWon;

    public BattleSystem(Battle battle) {
        this.battle = battle;
    }

    /**
     * Called at the start of any battle to perform initialization activities
     */
    public void startBattle() {
        round = 1;
        // prepare all of the mobs for battle. This will set their
        // battle stats. Also, shuffle all of their decks.
        for (Mob mob : battle.getAllBattleParticipants()) {
            mob.prepareForBattle();
            mob.getDeck().shuffle();
        }
    }

    /**
     * This method is called if the player is playing a card that has
     * no enemy target (like buffs).
     * @param card
     */
    public BattleRound performBattleRound(Card card) {
        return performBattleRound(card,null);
    }

    /**
     * This method is called if the player is playing a card that can be
     * used against a monster.
     * @param card
     * @param monster
     */
    public BattleRound performBattleRound(Card card, Monster monster) {
        round++;
        BattleRound battleRound = new BattleRound(this.round);
        Log.debug("=========( Round " + this.round + " )==============");


        // see who got initiate and let them go first
        if (battle.isPlayerInitiate()) {
            doPlayerRoundAndCheckIfPlayerWon(card, monster, battleRound);
        } else {
            doEnemyRoundAndCheckIfPlayerLost(battleRound);
        }

        // now, let the other side attack
        if (battle.isPlayerInitiate()) {
            doEnemyRoundAndCheckIfPlayerLost(battleRound);
        } else {
            doPlayerRoundAndCheckIfPlayerWon(card, monster, battleRound);
        }

        // process all status effects on all battle participants
        processStatusEffects(battleRound);

        // remove any dead monsters from the battle
        removeDeadMonstersFromBattle();
        
        // see if the player lost or won
        if (battle.getPlayer().isDead()) {
            playerLost();
        }

        if (battle.allEnemiesDead()) {
            playerWon();
        }

        // if the battle isn't over, make all the monsters draw cards
        // from their decks into their hands.  The player's cards will be drawn
        // by the UI
        if ( !battleOver ) {
            drawNewMonsterCards();
        }


        Log.debug("Round " + this.round + " is over");
        return battleRound;
    }

    private void drawNewMonsterCards() {
        for ( Mob mob : battle.getEnemies() ) {
            mob.getHand().drawToMax();
        }
    }

    private void processStatusEffects(BattleRound battleRound) {
        for ( Mob mob : battle.getAllBattleParticipants() ) {
            List<StatusEffect> statusEffects = mob.getStatusEffects();
            List<StatusEffect> statusEffectsToRemove = new ArrayList<StatusEffect>();
            for ( StatusEffect statusEffect : statusEffects ) {
                statusEffect.roundOccurred(mob,battleRound);
                if ( !statusEffect.isActive() ) {
                    statusEffect.effectRemoved(mob,battleRound);
                    statusEffectsToRemove.add(statusEffect);
                }
            }
            for ( StatusEffect statusEffect : statusEffectsToRemove ) {
                mob.removeStatusEffect(statusEffect);
            }
        }
    }

    private void doPlayerRoundAndCheckIfPlayerWon(Card card, Monster monster, BattleRound battleRound) {
        // play the card
        battleRound.addCardPlayedBy(battle.getPlayer(),card);
        card.usedBy(battle.getPlayer(),battleRound);
        if ( monster != null ) {
            card.usedAgainst(battle.getPlayer(),monster,battleRound);
        }
    }

    private void doEnemyRoundAndCheckIfPlayerLost(BattleRound battleRound) {
        for (Mob enemy : battle.getEnemies()) {
            Monster monster = (Monster)enemy;
            // play the card
            Card enemyCard = monster.chooseCardToPlay();
            battleRound.addCardPlayedBy(enemy,enemyCard);
            enemyCard.usedBy(enemy,battleRound);
            if ( enemyCard.isTargetable() ) {
                enemyCard.usedAgainst(enemy,battle.getPlayer(),battleRound);
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
