package com.wwflgames.fury.main;

import com.wwflgames.fury.map.Dungeon;
import com.wwflgames.fury.map.DungeonMap;
import com.wwflgames.fury.player.Player;

public interface AppState {
    Player getPlayer();

    void setPlayer(Player player);

    DungeonMap getMap();

    boolean doesPlayerHaveInitiative();

    void setPlayerInitiative(boolean flag);

    Dungeon getDungeon();

    void setDungeon(Dungeon dungeon);

    void setGameOver(boolean flag);

    boolean isGameOver();

    void setHelpReturnScreen(int state);

    int getHelpReturnScreen();
}
