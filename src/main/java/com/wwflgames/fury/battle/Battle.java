package com.wwflgames.fury.battle;

import com.wwflgames.fury.mob.Mob;
import com.wwflgames.fury.monster.Monster;

import java.util.ArrayList;
import java.util.List;

public class Battle {

    private Mob player;
    private List<Monster> enemies;
    private List<Monster> originalEnemies;
    private List<Mob> allBattleParticipants;
    private boolean playerInitiate;

    public Battle(Mob player, List<Monster> enemies, boolean playerInitiate) {
        this.player = player;
        this.enemies = enemies;
        this.playerInitiate = playerInitiate;
        originalEnemies = new ArrayList<Monster>();
        originalEnemies.addAll(enemies);
        allBattleParticipants = new ArrayList<Mob>();
        allBattleParticipants.add(player);
        allBattleParticipants.addAll(enemies);
    }

    public Mob getPlayer() {
        return player;
    }

    public List<Monster> getEnemies() {
        return enemies;
    }

    public List<Monster> getOriginalEnemies() {
        return originalEnemies;
    }

    public boolean isPlayerInitiate() {
        return playerInitiate;
    }

    public List<Mob> getAllBattleParticipants() {
        return allBattleParticipants;
    }

    public void removeEnemy(Mob enemyToRemove) {
        enemies.remove(enemyToRemove);
    }

    public boolean allEnemiesDead() {
        return enemies.isEmpty();
    }

    public int totalMonsterValue() {
        int value = 0;
        for (Monster monster : originalEnemies) {
            value += monster.monsterValue();
        }
        return value;
    }
}
