package com.wwflgames.fury.main;

import com.wwflgames.fury.map.Dungeon;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.player.Player;

// global game state object
public class AppStateImpl implements AppState {

    private Player player;
    private boolean playerInitiative;
    private Dungeon dungeon;
    private boolean gameOver;

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public DungeonMap getMap() {
        return dungeon.currentLevelMap();
    }

    @Override
    public boolean doesPlayerHaveInitiative() {
        return playerInitiative;
    }

    @Override
    public void setPlayerInitiative(boolean flag) {
        playerInitiative = flag;
    }

    @Override
    public Dungeon getDungeon() {
        return dungeon;
    }

    @Override
    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    @Override
    public void setGameOver(boolean flag) {
        this.gameOver = flag;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }


}
